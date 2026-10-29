package fr.alessdevita.valtherion.client;

import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import fr.alessdevita.valtherion.block.material.Material;
import fr.alessdevita.valtherion.client.audio.MusicTicker;
import fr.alessdevita.valtherion.client.audio.SoundHandler;
import fr.alessdevita.valtherion.client.entity.EntityClientPlayerMP;
import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.Gui;
import fr.alessdevita.valtherion.client.gui.GuiScreen;
import fr.alessdevita.valtherion.client.gui.GuiMainMenu;
import fr.alessdevita.valtherion.client.gui.GuiGameOver;
import fr.alessdevita.valtherion.client.gui.GuiIngameMenu;
import fr.alessdevita.valtherion.client.gui.GuiIngame;
import fr.alessdevita.valtherion.client.gui.GuiSleepMP;
import fr.alessdevita.valtherion.client.gui.ScaledResolution;
import fr.alessdevita.valtherion.client.multiplayer.GuiConnecting;
import fr.alessdevita.valtherion.client.multiplayer.PlayerControllerMP;
import fr.alessdevita.valtherion.client.multiplayer.ServerData;
import fr.alessdevita.valtherion.client.multiplayer.WorldClient;
import fr.alessdevita.valtherion.client.network.NetHandlerLoginClient;
import fr.alessdevita.valtherion.client.network.NetHandlerPlayClient;
import fr.alessdevita.valtherion.client.particle.EffectRenderer;
import fr.alessdevita.valtherion.client.renderer.EntityRenderer;
import fr.alessdevita.valtherion.client.renderer.GLAllocation;
import fr.alessdevita.valtherion.client.renderer.ItemRenderer;
import fr.alessdevita.valtherion.client.renderer.RenderGlobal;
import fr.alessdevita.valtherion.client.renderer.entity.RenderManager;
import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import fr.alessdevita.valtherion.client.resources.ResourcePackRepository;
import fr.alessdevita.valtherion.client.resources.SkinManager;
import fr.alessdevita.valtherion.client.settings.GameSettings;
import fr.alessdevita.valtherion.client.settings.KeyBinding;
import fr.alessdevita.valtherion.client.shader.Framebuffer;
import fr.alessdevita.valtherion.crash.CrashReport;
import fr.alessdevita.valtherion.crash.CrashReportCategory;
import fr.alessdevita.valtherion.entity.EntityLivingBase;
import fr.alessdevita.valtherion.item.ItemStack;
import fr.alessdevita.valtherion.network.EnumConnectionState;
import fr.alessdevita.valtherion.network.core.NetworkManager;
import fr.alessdevita.valtherion.network.client.GameClient;
import fr.alessdevita.valtherion.network.handshake.client.C00Handshake;
import fr.alessdevita.valtherion.network.login.client.C00PacketLoginStart;
import fr.alessdevita.valtherion.network.play.client.C16PacketClientStatus;
import fr.alessdevita.valtherion.profiler.IPlayerUsage;
import fr.alessdevita.valtherion.profiler.PlayerUsageSnooper;
import fr.alessdevita.valtherion.server.integrated.IntegratedServer;
import fr.alessdevita.valtherion.stats.AchievementList;
import fr.alessdevita.valtherion.stats.IStatStringFormat;
import fr.alessdevita.valtherion.stats.StatFileWriter;
import fr.alessdevita.valtherion.utils.MovingObjectPosition;
import fr.alessdevita.valtherion.utils.ReportedException;
import fr.alessdevita.valtherion.utils.Session;
import fr.alessdevita.valtherion.utils.Timer;
import fr.alessdevita.valtherion.utils.Util;
import fr.alessdevita.valtherion.world.chunk.storage.AnvilSaveConverter;
import fr.alessdevita.valtherion.world.storage.ISaveFormat;
import fr.alessdevita.valtherion.world.storage.ISaveHandler;
import fr.alessdevita.valtherion.world.storage.WorldInfo;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Client principal Valtherion (portage Minecraft.java -> LWJGL 3 / GLFW),
 * aligne sur l'API reelle du projet :
 *  - Gui de base commun (GuiMainMenu extends Gui, GuiScreen extends Gui)
 *  - Framebuffer(int, int)
 *  - FontRenderer(GameSettings, String, TextureManager, boolean)
 *  - GuiIngameMenu(Valtherion, int, int), GuiGameOver() sans arguments
 *  - TODO: profiler/snooper avances, metadata des resource packs
 */
public class Valtherion implements IPlayerUsage {
    private static final Logger logger = LoggerFactory.getLogger(Valtherion.class);
    private static final String LOCATION_LOGO = "textures/gui/title/devita.png";
    public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;

    /** Preallocation de 10 Mio pour garantir une taille de tas raisonnable. */
    public static byte[] memoryReserve = new byte[10485760];
    private final File fileResourcepacks;
    private final Multimap userProperties;
    private ServerData currentServerData;
    private String currentWorldFolder;
    private WorldInfo currentWorldInfo;

    /** Le TextureManager du client. */
    private TextureManager renderEngine;

    /** Defini a 'this' dans le constructeur ; utilise par des getters statiques. */
    private static Valtherion theValtherion;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean hasCrashed;

    /** Instance de CrashReport. */
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;

    /** Identifiant de la fenetre GLFW (equivalent Display). */
    private long window;
    private Timer timer = new Timer(20.0F);

    /** Instance de PlayerUsageSnooper (constructeur sans arguments dans ce projet). */
    private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper(); // TODO: PlayerUsageSnooper("client", this, ...)
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    public EntityClientPlayerMP thePlayer;
    public GuiIngame ingameGUI;

    /**
     * L'entite depuis laquelle le renderer determine le point de vue.
     */
    public EntityLivingBase renderViewEntity;
    public EffectRenderer effectRenderer;
    private final Session session;
    private boolean isGamePaused;

    /** Le renderer de police (chemin de texture en String dans ce projet). */
    public FontRenderer fontRenderer;

    /** Le GUI affiche actuellement (type commun Gui : GuiMainMenu ET GuiScreen en heritent). */
    public Gui currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;

    /** Compteur de clic gauche souris. */
    private int leftClickCounter;

    /** Largeur/hauteur temporaires pour le retour du plein ecran. */
    private int tempDisplayWidth;
    private int tempDisplayHeight;

    /** Instance de l'IntegratedServer. */
    private IntegratedServer theIntegratedServer;

    /** Sauter le rendu du monde. */
    public boolean skipRenderWorld;

    /** Le raycast survole par la souris. */
    public MovingObjectPosition objectMouseOver;

    /** Les parametres du jeu actifs. */
    public GameSettings gameSettings;

    /** Helper souris/clavier (encapsule les evenements GLFW). */
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final Proxy proxy;
    private ISaveFormat saveLoader;

    /** FPS affiche sur l'ecran de debug. */
    private static int debugFPS;

    /**
     * Quand on pose un bloc, mis a 6, decremente a chaque tick ;
     * quand il vaut 0, on peut en poser un autre.
     */
    private int rightClickDelayTimer;

    /** Si vrai, les textures seront rechargees au prochain tour de boucle. */
    private boolean refreshTexturePacksScheduled;
    private String serverName;
    private int serverPort;

    /** Si vrai, souris et clavier affectent le joueur au lieu des menus. */
    public boolean inGameHasFocus;
    long systemTime = getSystemTime();

    /** Compteur pour le join du joueur dans son environnement. */
    private int joinPlayerCounter;
    private final boolean jvm64bit;
    private final boolean isDemo;
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;

    /** Compteur local de mises a jour de chunks (WorldRenderer.chunksUpdated n'existe pas ici). */
    public static int chunksUpdated;

    private Framebuffer mcFramebuffer;
    private ResourcePackRepository mcResourcePackRepository;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private SkinManager skinManager;
    /** Emplacement de la texture du logo de chargement. */
    private String logoTextureLocation; // String : TextureManager.bindTexture(String) dans ce projet
    private final Queue scheduledTasks = Queues.newArrayDeque();
    private final Thread clientThread = Thread.currentThread();

    /** Vrai tant que la boucle de jeu doit tourner. */
    volatile boolean running = true;

    /** Chaine d'informations de debug. */
    public String debug = "";

    /** Derniere mise a jour (ms) de la chaine de debug. */
    long debugUpdateTime = getSystemTime();

    /** FPS courant. */
    int fpsCounter;
    long prevFrameTime = -1L;

    public Valtherion(Session sessionIn, int widthIn, int heightIn, boolean fullscreenIn, boolean isDemoIn,
                      File dataDirIn, File assetsDirIn, File resourcePacksDirIn, Proxy proxyIn,
                      String versionIn, Multimap userPropertiesIn)
    {
        theValtherion = this;
        this.mcDataDir = dataDirIn;
        this.fileAssets = assetsDirIn;
        this.fileResourcepacks = resourcePacksDirIn;
        this.launchedVersion = versionIn;
        this.userProperties = userPropertiesIn;
        this.proxy = proxyIn == null ? Proxy.NO_PROXY : proxyIn;
        this.session = sessionIn;
        logger.info("Setting user: " + sessionIn.getUsername());
        logger.info("(Session ID is " + sessionIn.getSessionID() + ")");
        this.isDemo = isDemoIn;
        this.displayWidth = widthIn;
        this.displayHeight = heightIn;
        this.tempDisplayWidth = widthIn;
        this.tempDisplayHeight = heightIn;
        this.fullscreen = fullscreenIn;
        this.jvm64bit = isJvm64bit();
        ImageIO.setUseCache(false);
        fr.alessdevita.valtherion.init.Bootstrap.register();
    }

    private static boolean isJvm64bit()
    {
        String[] keys = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

        for (String key : keys)
        {
            String value = System.getProperty(key);

            if (value != null && value.contains("64"))
            {
                return true;
            }
        }

        return false;
    }

    public Framebuffer getFramebuffer()
    {
        return this.mcFramebuffer;
    }

    public void crashed(CrashReport crashReportIn)
    {
        this.hasCrashed = true;
        this.crashReporter = crashReportIn;
    }

    /**
     * Affiche le rapport de crash et quitte.
     */
    public void displayCrashReport(CrashReport crashReportIn)
    {
        File crashDir = new File(getValtherion().mcDataDir, "crash-reports");
        File crashFile = new File(crashDir, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        System.out.println(crashReportIn.getCompleteReport());

        if (crashReportIn.getFile() != null)
        {
            System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(crashFile))
        {
            System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + crashFile.getAbsolutePath());
            System.exit(-1);
        }
        else
        {
            System.out.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public void setServer(String serverNameIn, int serverPortIn)
    {
        this.serverName = serverNameIn;
        this.serverPort = serverPortIn;
    }

    /**
     * Demarre le jeu : GLFW, fenetre, parametres, GL, ressources.
     * Equivalent LWJGL2 startGame() : Display.create() -> glfwCreateWindow().
     */
    private void startGame()
    {
        // --- Initialisation GLFW ---
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);

        this.gameSettings = new GameSettings(this, this.mcDataDir);

        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0)
        {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }

        long monitor = NULL;

        if (this.fullscreen)
        {
            monitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            this.displayWidth = vidMode.width();
            this.displayHeight = vidMode.height();

            if (this.displayWidth <= 0)
            {
                this.displayWidth = 1;
            }

            if (this.displayHeight <= 0)
            {
                this.displayHeight = 1;
            }
        }

        // --- Creation de la fenetre (equivalent Display.create) ---
        this.window = glfwCreateWindow(this.displayWidth, this.displayHeight, "Valtherion", monitor, NULL);

        if (this.window == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Centrage de la fenetre
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(this.window,
                    (vidmode.width() - this.displayWidth) / 2,
                    (vidmode.height() - this.displayHeight) / 2);
        }

        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(0); // vsync gere plus bas
        glfwShowWindow(this.window);
        GL.createCapabilities(); // equivalent GLContext.createCapabilities()

        // Icone de fenetre
        try
        {
            InputStream icon16 = this.openWindowIconStream("icons/icon_16x16.png");
            InputStream icon32 = this.openWindowIconStream("icons/icon_32x32.png");

            if (icon16 != null && icon32 != null)
            {
                // TODO: glfwSetWindowIcon(window, GLFWImage.buffer(...)) avec les images chargees
                logger.info("Window icons available (TODO: glfwSetWindowIcon)");
            }
        }
        catch (IOException e)
        {
            logger.error("Couldn't set icon", e);
        }

        // --- Rattachement des callbacks clavier/souris ---
        this.setupCallbacks();

        // TODO: OpenGlHelper.initializeTextures() n'existe pas encore : init textures Valtherion ici

        // Framebuffer(int, int) dans ce projet (pas de flag depth)
        this.mcFramebuffer = new Framebuffer(this.displayWidth, this.displayHeight);
        // TODO: mcFramebuffer.setFramebufferColor(0, 0, 0, 0) si le framebuffer le supporte

        // TODO: guiAchievement (toast) une fois GuiAchievement branche sur le Gui de base
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
        this.mcResourcePackRepository = null; // TODO: ResourcePackRepository(fileResourcepacks, defaultPack, gameSettings)
        this.renderEngine = new TextureManager(); // TODO: passer l'IResourceManager au TextureManager
        this.skinManager = new SkinManager();    // TODO: passer (renderEngine, dossier skins)
        this.loadScreen();
        this.mcSoundHandler = new SoundHandler(); // TODO: passer (resourceManager, gameSettings)
        this.mcMusicTicker = new MusicTicker();   // TODO: passer le client
        // FontRenderer avec un chemin de texture en String dans ce projet
        this.fontRenderer = new FontRenderer(this.gameSettings, "textures/font/ascii.png", this.renderEngine, false);
        // TODO: flag unicode/bidi une fois LanguageManager implemente

        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        this.entityRenderer = new EntityRenderer(this);
        this.mouseHelper = new MouseHelper(this.window);
        this.checkGLError("Pre startup");
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glClearDepth(1.0D);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.1F);
        glCullFace(GL_BACK);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glMatrixMode(GL_MODELVIEW);
        this.checkGLError("Startup");
        this.renderGlobal = new RenderGlobal(this);
        glViewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        this.checkGLError("Post startup");

        if (this.serverName != null)
        {
            this.displayGuiScreen(new GuiConnecting());
        }
        else
        {
            this.displayGuiScreen(new GuiMainMenu());
        }

        this.loadingScreen = new LoadingScreenRenderer(this, this.window, this.displayWidth, this.displayHeight);

        if (this.gameSettings.fullScreen && !this.fullscreen)
        {
            this.toggleFullscreen();
        }

        // VSync : GLFW utilise glfwSwapInterval(1/0) au lieu de Display.setVSyncEnabled
        glfwSwapInterval(this.gameSettings.enableVsync ? 1 : 0);
    }

    /**
     * Rattache tous les callbacks GLFW (clavier, souris, fenetre).
     * Les evenements sont buffered puis consommes dans runTick().
     */
    private void setupCallbacks()
    {
        glfwSetKeyCallback(this.window, (windowHandle, key, scanCode, action, modifiers) ->
        {
            if (this.mouseHelper != null && key != GLFW_KEY_UNKNOWN)
            {
                this.mouseHelper.addKeyboardEvent(key, action);
            }
        });

        glfwSetMouseButtonCallback(this.window, (windowHandle, button, action, modifiers) ->
        {
            if (this.mouseHelper != null)
            {
                this.mouseHelper.addMouseButtonEvent(button, action);
            }
        });

        glfwSetScrollCallback(this.window, (windowHandle, xOffset, yOffset) ->
        {
            if (this.mouseHelper != null && yOffset != 0.0D)
            {
                this.mouseHelper.addScrollEvent(yOffset > 0.0D ? 1 : -1);
            }
        });

        glfwSetFramebufferSizeCallback(this.window, (windowHandle, width, height) ->
        {
            if (width > 0 && height > 0)
            {
                this.resize(width, height);
            }
        });

        glfwSetWindowFocusCallback(this.window, (windowHandle, focused) ->
        {
            if (!focused)
            {
                this.setIngameNotInFocus();
            }
        });
    }

    private InputStream openWindowIconStream(String iconName) throws IOException
    {
        // TODO: rebrancher sur le DefaultResourcePack une fois son API (getInputStream) livree
        File[] candidates = new File[] {
                new File("valtherion/assets/valtherion/" + iconName),
                new File("assets/valtherion/" + iconName),
                new File(this.mcDataDir, "assets/valtherion/" + iconName)
        };

        for (File file : candidates)
        {
            if (file.isFile())
            {
                return new java.io.FileInputStream(file);
            }
        }

        return null;
    }

    /**
     * Affiche l'ecran de chargement (fond degrade sombre + logo).
     */
    private void loadScreen()
    {
        ScaledResolution scaledRes = new ScaledResolution(this, this.displayWidth, this.displayHeight);
        Framebuffer framebuffer = new Framebuffer(this.displayWidth, this.displayHeight);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0D, (double) scaledRes.getScaledWidth(), (double) scaledRes.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.0F, 0.0F, -2000.0F);
        glDisable(GL_LIGHTING);
        glDisable(GL_FOG);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        // Fond degrade sombre "Valtherion" (noir/rouge fonce), dessine en GL direct
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBegin(GL_QUADS);
        glColor4f(0.06F, 0.0F, 0.0F, 1.0F);
        glVertex2f(0.0F, (float) scaledRes.getScaledHeight());
        glColor4f(0.10F, 0.0F, 0.0F, 1.0F);
        glVertex2f((float) scaledRes.getScaledWidth(), (float) scaledRes.getScaledHeight());
        glColor4f(0.02F, 0.0F, 0.0F, 1.0F);
        glVertex2f((float) scaledRes.getScaledWidth(), 0.0F);
        glColor4f(0.04F, 0.0F, 0.0F, 1.0F);
        glVertex2f(0.0F, 0.0F);
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);

        // TODO: logo de chargement : renderEngine.getDynamicTextureLocation("logo", new DynamicTexture(...))
        //       une fois DynamicTexture(BufferedImage) supporte ; bindTexture prend un String dans ce projet.

        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glDisable(GL_LIGHTING);
        glDisable(GL_FOG);
        glFlush();
        this.updateDisplay();
    }

    /**
     * Retourne le save loader courant.
     */
    public ISaveFormat getSaveLoader()
    {
        return this.saveLoader;
    }

    /**
     * Definit le GUI passe en argument comme ecran principal.
     * Le parametre est le type commun Gui (GuiMainMenu et GuiScreen en heritent).
     */
    public void displayGuiScreen(Gui guiScreenIn)
    {
        if (this.currentScreen instanceof GuiScreen)
        {
            ((GuiScreen) this.currentScreen).onGuiClosed();
        }

        if (guiScreenIn == null && this.theWorld == null)
        {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.thePlayer != null && this.thePlayer.getHealth() <= 0.0F)
        {
            guiScreenIn = new GuiGameOver(this, this.displayWidth, this.displayHeight, 0);
        }

        this.currentScreen = guiScreenIn;

        if (guiScreenIn instanceof GuiScreen)
        {
            this.setIngameNotInFocus();
            ScaledResolution scaledRes = new ScaledResolution(this, this.displayWidth, this.displayHeight);
            ((GuiScreen) guiScreenIn).setWorldAndResolution(this, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
            this.skipRenderWorld = this.theWorld == null;
        }
        else
        {
            this.setIngameNotInFocus();
            if (guiScreenIn != null)
            {
                guiScreenIn.setValtherion(this);
                guiScreenIn.init();
                this.skipRenderWorld = this.theWorld == null;
            }
            else
            {
                this.setIngameFocus();
            }
        }
    }

    /**
     * Verifie une erreur OpenGL, l'affiche le cas echeant.
     */
    private void checkGLError(String spot)
    {
        int i = glGetError();

        if (i != 0)
        {
            logger.error("########## GL ERROR ##########");
            logger.error("@ " + spot);
            logger.error("GL error code: " + i);
        }
    }

    /**
     * Eteint le client : arrete les sons, libere GL et GLFW.
     */
    public void shutdownClient()
    {
        try
        {
            logger.info("Stopping!");

            try
            {
                this.loadWorld((WorldClient) null);
            }
            catch (Throwable ignored)
            {
                ;
            }

            try
            {
                GLAllocation.deleteTexturesAndDisplayLists();
            }
            catch (Throwable ignored)
            {
                ;
            }
        }
        finally
        {
            // Equivalent Display.destroy() en GLFW :
            Callbacks.glfwFreeCallbacks(this.window);
            glfwDestroyWindow(this.window);
            glfwTerminate();
            GLFWErrorCallback callback = glfwSetErrorCallback(null);
            if (callback != null)
            {
                callback.free();
            }

            if (!this.hasCrashed)
            {
                System.exit(0);
            }
        }

        System.gc();
    }

    public void run()
    {
        this.running = true;
        CrashReport crashReport;

        try
        {
            this.startGame();
        }
        catch (Throwable throwableStart)
        {
            crashReport = CrashReport.makeCrashReport(throwableStart, "Initializing game");
            crashReport.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashReport));
            return;
        }

        while (true)
        {
            try
            {
                while (this.running)
                {
                    if (!this.hasCrashed || this.crashReporter == null)
                    {
                        try
                        {
                            this.runGameLoop();
                        }
                        catch (OutOfMemoryError outofmemoryerror)
                        {
                            this.freeMemory();
                            this.displayGuiScreen((Gui) null);
                            System.gc();
                        }

                        continue;
                    }

                    this.displayCrashReport(this.crashReporter);
                    return;
                }
            }
            catch (OutOfMemoryError e)
            {
                ;
            }
            catch (ReportedException reportedexception)
            {
                this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
                this.freeMemory();
                logger.error("Reported exception thrown!", reportedexception);
                this.displayCrashReport(reportedexception.getCrashReport());
            }
            catch (Throwable throwableLoop)
            {
                crashReport = this.addGraphicsAndWorldToCrashReport(CrashReport.makeCrashReport(throwableLoop, "Unexpected error"));
                this.freeMemory();
                logger.error("Unreported exception thrown!", throwableLoop);
                this.displayCrashReport(crashReport);
            }
            finally
            {
                this.shutdownClient();
            }

            return;
        }
    }

    /**
     * Appele en boucle depuis run().
     */
    private void runGameLoop()
    {
        if (glfwWindowShouldClose(this.window))
        {
            this.shutdown();
        }

        if (this.isGamePaused && this.theWorld != null)
        {
            float f = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = f;
        }
        else
        {
            this.timer.updateTimer();
        }

        if ((this.theWorld == null || this.currentScreen == null) && this.refreshTexturePacksScheduled)
        {
            this.refreshTexturePacksScheduled = false;
            // TODO: refreshResources() une fois le systeme de resource packs branche
        }

        this.checkGLError("Pre render");
        this.mcSoundHandler.update(1.0F / 20.0F);

        glPushMatrix();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);

        if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock())
        {
            this.gameSettings.thirdPersonView = 0;
        }

        if (!this.skipRenderWorld)
        {
            this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
        }

        this.renderCurrentScreen();

        glFlush();

        // Equivalent Display.isActive() : focus de la fenetre GLFW
        if (!this.isWindowFocused() && this.fullscreen)
        {
            this.toggleFullscreen();
        }

        glPopMatrix();
        this.updateDisplay();
        Thread.yield();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        this.isGamePaused = this.isSingleplayer() && this.currentScreen instanceof GuiScreen
                && ((GuiScreen) this.currentScreen).doesGuiPauseGame(); // TODO: && !theIntegratedServer.getPublic()

        while (getSystemTime() >= this.debugUpdateTime + 1000L)
        {
            debugFPS = this.fpsCounter;
            this.debug = debugFPS + " fps, " + chunksUpdated + " chunk updates";
            chunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            // TODO: stats memoire du snooper une fois PlayerUsageSnooper implemente
        }

        if (this.isFramerateLimitBelowMax())
        {
            this.syncDisplay(this.getLimitFramerate());
        }
    }

    /**
     * Equivalent Display.update() : poll des evenements + swap buffers.
     */
    public void updateDisplay()
    {
        glfwPollEvents();
        glfwSwapBuffers(this.window);
    }

    /** Rend l'écran actif dans une projection 2D mise à l'échelle. */
    private void renderCurrentScreen()
    {
        if (this.currentScreen == null)
        {
            return;
        }

        ScaledResolution resolution = new ScaledResolution(this, this.displayWidth, this.displayHeight);
        double[] cursorX = new double[1];
        double[] cursorY = new double[1];
        glfwGetCursorPos(this.window, cursorX, cursorY);
        int mouseX = (int) (cursorX[0] * resolution.getScaledWidth() / Math.max(1, this.displayWidth));
        int mouseY = (int) (cursorY[0] * resolution.getScaledHeight() / Math.max(1, this.displayHeight));

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0.0D, resolution.getScaledWidth(), resolution.getScaledHeight(), 0.0D, -1000.0D, 1000.0D);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        if (this.currentScreen instanceof GuiScreen)
        {
            ((GuiScreen) this.currentScreen).drawScreen(mouseX, mouseY, this.timer.renderPartialTicks);
        }
        else
        {
            this.currentScreen.render();
        }
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }

    /**
     * Limitation de framerate : GLFW n'a qu'un swapInterval binaire,
     * donc frame-limit manuel (sleep calibre).
     */
    private long lastSyncTime;
    private void syncDisplay(int fps)
    {
        if (fps <= 0)
        {
            return;
        }

        long targetNanos = 1_000_000_000L / fps;
        long now = System.nanoTime();
        long elapsed = now - this.lastSyncTime;

        if (elapsed < targetNanos)
        {
            try
            {
                Thread.sleep((targetNanos - elapsed) / 1_000_000L);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }

        this.lastSyncTime = System.nanoTime();
    }

    /** Equivalent Display.isActive(). */
    public boolean isWindowFocused()
    {
        return glfwGetWindowAttrib(this.window, GLFW_FOCUSED) == GLFW_TRUE;
    }

    public int getLimitFramerate()
    {
        return this.theWorld == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
    }

    public boolean isFramerateLimitBelowMax()
    {
        // GameSettings.Options.FRAMERATE_LIMIT n'existe pas : seuil "unlimited" de MC = 260 fps
        return this.getLimitFramerate() < 260;
    }

    public void freeMemory()
    {
        try
        {
            memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        }
        catch (Throwable ignored)
        {
            ;
        }

        try
        {
            System.gc();
        }
        catch (Throwable ignored)
        {
            ;
        }

        try
        {
            System.gc();
            this.loadWorld((WorldClient) null);
        }
        catch (Throwable ignored)
        {
            ;
        }

        System.gc();
    }

    /**
     * Appele quand la fenetre se ferme : arrete la boucle proprement.
     */
    public void shutdown()
    {
        this.running = false;
    }

    /**
     * Donne le focus au jeu si la fenetre est active. Vide aussi le GUI courant.
     */
    public void setIngameFocus()
    {
        if (this.isWindowFocused())
        {
            if (!this.inGameHasFocus)
            {
                this.inGameHasFocus = true;
                this.mouseHelper.grabMouseCursor();
                this.displayGuiScreen((Gui) null);
                this.leftClickCounter = 10000;
            }
        }
    }

    /**
     * Reinitialise l'etat clavier, desactive le focus et libere le curseur.
     */
    public void setIngameNotInFocus()
    {
        if (this.inGameHasFocus)
        {
            // TODO: KeyBinding.unPressAllKeys() une fois la methode ajoutee au KeyBinding
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }

    /**
     * Affiche le menu en jeu.
     */
    public void displayInGameMenu()
    {
        if (this.currentScreen == null)
        {
            this.displayGuiScreen(new GuiIngameMenu(this, this.displayWidth, this.displayHeight));
            // TODO: pause des sons en singleplayer (mcSoundHandler.pauseSounds())
        }
    }

    private void clickMouseOrBlock(boolean leftClickHeld)
    {
        if (!leftClickHeld)
        {
            this.leftClickCounter = 0;
        }

        if (this.leftClickCounter <= 0)
        {
            if (leftClickHeld && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = this.objectMouseOver.blockX;
                int y = this.objectMouseOver.blockY;
                int z = this.objectMouseOver.blockZ;

                if (this.theWorld.getBlock(x, y, z).getMaterial() != Material.air)
                {
                    this.playerController.onPlayerDamageBlock(x, y, z, this.objectMouseOver.sideHit);

                    if (this.thePlayer.isCurrentToolAdventureModeExempt(x, y, z))
                    {
                        this.effectRenderer.addBlockHitEffects(x, y, z, this.objectMouseOver.sideHit);
                        this.thePlayer.swingItem();
                    }
                }
            }
            else
            {
                this.playerController.resetBlockRemoving();
            }
        }
    }

    private void clickMouse()
    {
        if (this.leftClickCounter <= 0)
        {
            this.thePlayer.swingItem();

            if (this.objectMouseOver == null)
            {
                logger.error("Null returned as 'hitResult', this shouldn't happen!");

                if (this.playerController.isNotCreative())
                {
                    this.leftClickCounter = 10;
                }
            }
            else
            {
                switch (this.objectMouseOver.typeOfHit)
                {
                    case ENTITY:
                        this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        int x = this.objectMouseOver.blockX;
                        int y = this.objectMouseOver.blockY;
                        int z = this.objectMouseOver.blockZ;

                        if (this.theWorld.getBlock(x, y, z).getMaterial() == Material.air)
                        {
                            if (this.playerController.isNotCreative())
                            {
                                this.leftClickCounter = 10;
                            }
                        }
                        else
                        {
                            this.playerController.clickBlock(x, y, z, this.objectMouseOver.sideHit);
                        }
                }
            }
        }
    }

    private void rightClickMouse()
    {
        this.rightClickDelayTimer = 4;
        boolean flag = true;
        ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();

        if (this.objectMouseOver == null)
        {
            logger.warn("Null returned as 'hitResult', this shouldn't happen!");
        }
        else
        {
            switch (this.objectMouseOver.typeOfHit)
            {
                case ENTITY:
                    if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit))
                    {
                        flag = false;
                    }

                    break;

                case BLOCK:
                    int x = this.objectMouseOver.blockX;
                    int y = this.objectMouseOver.blockY;
                    int z = this.objectMouseOver.blockZ;

                    if (this.theWorld.getBlock(x, y, z).getMaterial() != Material.air)
                    {
                        int stackSize = itemstack != null ? itemstack.stackSize : 0;

                        if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, x, y, z, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec))
                        {
                            flag = false;
                            this.thePlayer.swingItem();
                        }

                        if (itemstack == null)
                        {
                            return;
                        }

                        if (itemstack.stackSize == 0)
                        {
                            this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                        }
                        else if (itemstack.stackSize != stackSize || this.playerController.isInCreativeMode())
                        {
                            this.entityRenderer.itemRenderer.resetEquippedProgress();
                        }
                    }
            }
        }

        if (flag)
        {
            ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();

            if (itemstack1 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemstack1))
            {
                this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }

    /**
     * Bascule le plein ecran via GLFW.
     */
    public void toggleFullscreen()
    {
        try
        {
            this.fullscreen = !this.fullscreen;

            if (this.fullscreen)
            {
                long monitor = glfwGetPrimaryMonitor();
                GLFWVidMode vidmode = glfwGetVideoMode(monitor);
                glfwSetWindowMonitor(this.window, monitor, 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
                this.displayWidth = vidmode.width();
                this.displayHeight = vidmode.height();
            }
            else
            {
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;

                try (MemoryStack stack = MemoryStack.stackPush())
                {
                    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                    glfwSetWindowMonitor(this.window, NULL,
                            (vidmode.width() - this.displayWidth) / 2,
                            (vidmode.height() - this.displayHeight) / 2,
                            this.displayWidth, this.displayHeight, vidmode.refreshRate());
                }

                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }
            }

            if (this.currentScreen instanceof GuiScreen)
            {
                this.resize(this.displayWidth, this.displayHeight);
            }
            else
            {
                this.updateFramebufferSize();
            }

            glfwSwapInterval(this.gameSettings.enableVsync ? 1 : 0);
            this.updateDisplay();
        }
        catch (Exception exception)
        {
            logger.error("Couldn't toggle fullscreen", exception);
        }
    }

    /**
     * Redimensionne l'ecran courant.
     */
    private void resize(int width, int height)
    {
        this.displayWidth = width <= 0 ? 1 : width;
        this.displayHeight = height <= 0 ? 1 : height;

        if (this.currentScreen instanceof GuiScreen)
        {
            ScaledResolution scaledRes = new ScaledResolution(this, width, height);
            ((GuiScreen) this.currentScreen).setWorldAndResolution(this, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
        }

        this.loadingScreen = new LoadingScreenRenderer(this, this.window, this.displayWidth, this.displayHeight);
        this.updateFramebufferSize();
    }

    private void updateFramebufferSize()
    {
        this.mcFramebuffer = new Framebuffer(this.displayWidth, this.displayHeight);
        // TODO: entityRenderer.updateShaderGroupSize(width, height) si les shaders sont branches
    }

    /**
     * Execute un tick.
     * Mouse.next()/Keyboard.next() sont remplaces par la consommation des
     * files d'evenements buffered remplies par les callbacks GLFW.
     */
    public void runTick()
    {
        synchronized (this.scheduledTasks)
        {
            while (!this.scheduledTasks.isEmpty())
            {
                ((FutureTask) this.scheduledTasks.poll()).run();
            }
        }

        if (this.rightClickDelayTimer > 0)
        {
            --this.rightClickDelayTimer;
        }

        if (!this.isGamePaused && this.ingameGUI != null)
        {
            this.ingameGUI.updateTick(); // TODO: brancher GuiIngame
        }

        this.entityRenderer.getMouseOver(1.0F);

        if (!this.isGamePaused && this.theWorld != null)
        {
            this.playerController.updateController();
        }

        if (!this.isGamePaused)
        {
            this.renderEngine.tick();
        }

        if (this.currentScreen == null && this.thePlayer != null)
        {
            if (this.thePlayer.getHealth() <= 0.0F)
            {
                this.displayGuiScreen((Gui) null);
            }
            else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null)
            {
                this.displayGuiScreen(new GuiSleepMP(this, this.displayWidth, this.displayHeight));
            }
        }
        else if (this.currentScreen instanceof GuiSleepMP && this.thePlayer != null && !this.thePlayer.isPlayerSleeping())
        {
            this.displayGuiScreen((Gui) null);
        }

        if (this.currentScreen != null)
        {
            this.leftClickCounter = 10000;
        }

        CrashReport crashreport;
        CrashReportCategory crashreportcategory;

        if (this.currentScreen != null)
        {
            Gui gui = this.currentScreen;

            try
            {
                this.processGuiInput(gui);
            }
            catch (Throwable throwable)
            {
                crashreport = CrashReport.makeCrashReport(throwable, "Updating screen events");
                crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addCrashSectionCallable("Screen name", new Callable()
                {
                    public String call()
                    {
                        return Valtherion.this.currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }

            if (gui instanceof GuiScreen)
            {
                try
                {
                    ((GuiScreen) gui).updateScreen();
                }
                catch (Throwable throwable1)
                {
                    crashreport = CrashReport.makeCrashReport(throwable1, "Ticking screen");
                    crashreportcategory = crashreport.makeCategory("Affected screen");
                    crashreportcategory.addCrashSectionCallable("Screen name", new Callable()
                    {
                        public String call()
                        {
                            return Valtherion.this.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }

        if (this.currentScreen == null || this.currentScreen instanceof GuiScreen && ((GuiScreen) this.currentScreen).allowsUserInput())
        {
            // --- Evenements souris (file buffered alimentee par glfwSetMouseButtonCallback / scroll) ---
            while (this.mouseHelper.nextMouseEvent())
            {
                int button = this.mouseHelper.getEventButton();
                boolean state = this.mouseHelper.getEventButtonState();

                if (button >= 0)
                {
                    KeyBinding.setKeyBindState(button - 100, state);

                    if (state)
                    {
                        KeyBinding.onTick(button - 100);
                    }
                }

                long clickDelta = getSystemTime() - this.systemTime;

                if (clickDelta <= 200L)
                {
                    int wheel = this.mouseHelper.getEventDWheel();

                    if (wheel != 0 && this.thePlayer != null)
                    {
                        this.thePlayer.inventory.changeCurrentItem(wheel);
                    }

                    if (this.currentScreen == null)
                    {
                        if (button >= 0 && !this.inGameHasFocus && state)
                        {
                            this.setIngameFocus();
                        }
                    }
                    else if (this.currentScreen instanceof GuiScreen)
                    {
                        if (button >= 0)
                        {
                            ((GuiScreen) this.currentScreen).handleMouseInput(button,
                                    state ? GLFW_PRESS : GLFW_RELEASE);
                        }
                    }
                }
            }

            if (this.leftClickCounter > 0)
            {
                --this.leftClickCounter;
            }

            // --- Evenements clavier (file buffered alimentee par glfwSetKeyCallback) ---
            while (this.mouseHelper.nextKeyboardEvent())
            {
                int key = this.mouseHelper.getEventKey(); // GLFW_KEY_* (codes differents des codes LWJGL2 !)
                boolean keyState = this.mouseHelper.getEventKeyState();
                KeyBinding.setKeyBindState(key, keyState);

                if (keyState)
                {
                    KeyBinding.onTick(key);
                }

                if (keyState)
                {
                    if (key == GLFW_KEY_F8 && this.entityRenderer != null)
                    {
                        this.entityRenderer.deactivateShader();
                    }

                    if (this.currentScreen instanceof GuiScreen)
                    {
                        ((GuiScreen) this.currentScreen).handleKeyboardInput(key);
                    }
                    else if (this.currentScreen == null)
                    {
                        if (key == GLFW_KEY_ESCAPE)
                        {
                            this.displayInGameMenu();
                        }

                        if (key == GLFW_KEY_F1)
                        {
                            this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                        }

                        if (key == GLFW_KEY_F3)
                        {
                            this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                        }

                        if (this.gameSettings.keyBindTogglePerspective.isPressed())
                        {
                            ++this.gameSettings.thirdPersonView;

                            if (this.gameSettings.thirdPersonView > 2)
                            {
                                this.gameSettings.thirdPersonView = 0;
                            }
                        }

                        if (this.gameSettings.keyBindSmoothCamera.isPressed())
                        {
                            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                        }
                    }
                }
            }

            for (int i1 = 0; i1 < 9; ++i1)
            {
                if (this.gameSettings.keyBindsHotbar[i1].isPressed())
                {
                    this.thePlayer.inventory.currentItem = i1;
                }
            }

            while (this.gameSettings.keyBindInventory.isPressed())
            {
                this.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                // TODO: displayGuiScreen(new GuiInventory(this.thePlayer))
            }

            while (this.gameSettings.keyBindDrop.isPressed())
            {
                this.thePlayer.dropOneItem(false);
            }

            while (this.gameSettings.keyBindChat.isPressed())
            {
                // TODO: displayGuiScreen(new GuiChat())
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed())
            {
                // TODO: displayGuiScreen(new GuiChat("/"))
            }

            if (this.thePlayer != null && this.thePlayer.isUsingItem())
            {
                if (!this.gameSettings.keyBindUseItem.getIsKeyPressed())
                {
                    this.playerController.onStoppedUsingItem(this.thePlayer);
                }
            }
            else
            {
                while (this.gameSettings.keyBindAttack.isPressed())
                {
                    this.clickMouse();
                }

                while (this.gameSettings.keyBindUseItem.isPressed())
                {
                    this.rightClickMouse();
                }

                while (this.gameSettings.keyBindPickBlock.isPressed())
                {
                    this.pickerBlock();
                }
            }

            if (this.gameSettings.keyBindUseItem.getIsKeyPressed() && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem())
            {
                this.rightClickMouse();
            }

            this.clickMouseOrBlock(this.currentScreen == null && this.gameSettings.keyBindAttack.getIsKeyPressed() && this.inGameHasFocus);
        }

        if (this.theWorld != null)
        {
            if (this.thePlayer != null)
            {
                ++this.joinPlayerCounter;

                if (this.joinPlayerCounter == 30)
                {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }

            if (!this.isGamePaused)
            {
                this.entityRenderer.updateRenderer();
            }

            if (!this.isGamePaused)
            {
                this.theWorld.updateEntities();
            }
        }

        if (!this.isGamePaused)
        {
            this.mcMusicTicker.update(1.0F / 20.0F);
            this.mcSoundHandler.update(1.0F / 20.0F);
        }

        if (this.theWorld != null)
        {
            if (!this.isGamePaused)
            {
                try
                {
                    this.theWorld.tick();
                }
                catch (Throwable throwable3)
                {
                    crashreport = CrashReport.makeCrashReport(throwable3, "Exception in world tick");

                    if (this.theWorld == null)
                    {
                        crashreportcategory = crashreport.makeCategory("Affected level");
                        crashreportcategory.addCrashSection("Problem", "Level is null!");
                    }
                    else
                    {
                        this.theWorld.addWorldInfoToCrashReport(crashreport);
                    }

                    throw new ReportedException(crashreport);
                }
            }

            if (!this.isGamePaused)
            {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.myNetworkManager != null)
        {
            this.myNetworkManager.processReceivedPackets();
        }

        this.systemTime = getSystemTime();
    }

    /** Traite les entrées d'interface avant les entrées de jeu. */
    private void processGuiInput(Gui gui)
    {
        double[] cursorX = new double[1];
        double[] cursorY = new double[1];
        glfwGetCursorPos(this.window, cursorX, cursorY);
        int mouseX = (int) (cursorX[0] * gui.getWidth() / Math.max(1, this.displayWidth));
        int mouseY = (int) (cursorY[0] * gui.getHeight() / Math.max(1, this.displayHeight));

        while (this.mouseHelper.nextMouseEvent())
        {
            int button = this.mouseHelper.getEventButton();
            boolean pressed = this.mouseHelper.getEventButtonState();
            int wheel = this.mouseHelper.getEventDWheel();

            if (button >= 0)
            {
                KeyBinding.setKeyBindState(button - 100, pressed);
                if (pressed)
                {
                    KeyBinding.onTick(button - 100);
                    if (gui instanceof GuiScreen)
                    {
                        ((GuiScreen) gui).handleMouseInput(button, GLFW_PRESS);
                    }
                    else
                    {
                        gui.handleMouseInput(button, GLFW_PRESS, mouseX, mouseY);
                    }
                }
                else
                {
                    if (gui instanceof GuiScreen)
                    {
                        ((GuiScreen) gui).handleMouseInput(button, GLFW_RELEASE);
                    }
                    else
                    {
                        gui.handleMouseInput(button, GLFW_RELEASE, mouseX, mouseY);
                    }
                }
            }

            if (wheel != 0 && this.thePlayer != null)
            {
                this.thePlayer.inventory.changeCurrentItem(wheel);
            }
        }

        while (this.mouseHelper.nextKeyboardEvent())
        {
            int key = this.mouseHelper.getEventKey();
            boolean pressed = this.mouseHelper.getEventKeyState();
            if (key != GLFW_KEY_UNKNOWN)
            {
                KeyBinding.setKeyBindState(key, pressed);
                if (pressed)
                {
                    KeyBinding.onTick(key);
                    if (gui instanceof GuiScreen)
                    {
                        ((GuiScreen) gui).handleKeyboardInput(key);
                    }
                    else
                    {
                        if (key == GLFW_KEY_ESCAPE)
                        {
                            this.displayGuiScreen(new GuiMainMenu());
                        }
                        else
                        {
                            gui.handleKeyInput(key, GLFW_PRESS);
                        }
                    }
                }
            }
        }
    }

    /**
     * Lance le serveur integre : dossier du monde, nom, WorldSettings.
     */
    public void launchIntegratedServer(String folderName, String worldName, fr.alessdevita.valtherion.world.WorldSettings worldSettingsIn)
    {
        this.loadWorld((WorldClient) null);
        System.gc();
        ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && worldSettingsIn != null)
        {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }
        this.currentWorldFolder = folderName;
        this.currentWorldInfo = worldinfo;

        try
        {
            this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn);
            this.theIntegratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        this.loadingScreen.displayProgressMessage("menu.loadingLevel");

        while (!this.theIntegratedServer.serverIsInRunLoop())
        {
            // TODO: message d'avancement du serveur integre
            try
            {
                Thread.sleep(200L);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }

        if (worldinfo == null) worldinfo = new WorldInfo(worldSettingsIn, worldName);
        this.currentWorldInfo = worldinfo;
        this.currentWorldFolder = folderName;
        this.theWorld = new WorldClient();
        this.playerController = new PlayerControllerMP(this);
        this.thePlayer = this.playerController.createClientPlayer(this.theWorld, new StatFileWriter());
        this.renderViewEntity = this.thePlayer;
        this.theWorld.spawnEntityInWorld(this.thePlayer);
        this.thePlayer.sendQueue = new NetHandlerPlayClient();
        this.ingameGUI = new GuiIngame(this, this.displayWidth, this.displayHeight);

        this.displayGuiScreen((Gui) null);
        java.net.SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
        networkmanager.scheduleOutboundPacket(new C00Handshake(5, socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.scheduleOutboundPacket(new C00PacketLoginStart(this.getSession().getPlayerID()));
        this.myNetworkManager = networkmanager;
    }

    /**
     * Charge un monde (decharge d'abord le monde courant).
     */
    public void loadWorld(WorldClient worldClientIn)
    {
        this.loadWorld(worldClientIn, "");
    }

    /**
     * par2 affiche sur l'ecran de chargement ; decharge d'abord le monde courant.
     */
    public void loadWorld(WorldClient worldClientIn, String loadingMessage)
    {
        if (worldClientIn == null)
        {
            NetHandlerPlayClient nethandlerplayclient = this.getNetHandler();

            if (nethandlerplayclient != null)
            {
                nethandlerplayclient.cleanup();
            }

            if (this.theIntegratedServer != null)
            {
                this.theIntegratedServer.initiateShutdown();
            }

            this.theIntegratedServer = null;
        }

        this.renderViewEntity = null;
        this.myNetworkManager = null;

        if (this.loadingScreen != null)
        {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
        }

        if (worldClientIn == null && this.theWorld != null)
        {
            this.scheduleResourcesRefresh();
            this.setServerData((ServerData) null);
            this.integratedServerIsRunning = false;
        }

        this.theWorld = worldClientIn;

        if (worldClientIn != null)
        {
            if (this.renderGlobal != null)
            {
                this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
            }

            if (this.effectRenderer != null)
            {
                this.effectRenderer.clearEffects(worldClientIn);
            }

            if (this.thePlayer == null)
            {
                this.thePlayer = this.playerController.createClientPlayer(worldClientIn, new StatFileWriter());
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.thePlayer);
            this.thePlayer.movementInput = new fr.alessdevita.valtherion.utils.MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.thePlayer);
            this.renderViewEntity = this.thePlayer;
        }
        else
        {
            this.saveLoader.flushCache();
            this.thePlayer = null;
        }

        System.gc();
        this.systemTime = 0L;
    }

    /**
     * Infos de rendu du RenderGlobal (F3).
     */
    public String debugInfoRenders()
    {
        return this.renderGlobal.getDebugInfoRenders();
    }

    /**
     * Infos d'entites autour du joueur (F3).
     */
    public String getEntityDebug()
    {
        return this.renderGlobal.getDebugInfoEntities();
    }

    /**
     * Nom du chunk provider courant.
     */
    public String getWorldProviderName()
    {
        return this.theWorld.getProviderName();
    }

    /**
     * Nombre d'entites dans le monde (F3).
     */
    public String debugInfoEntities()
    {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
    }

    public void setDimensionAndSpawnPlayer(int dimension)
    {
        this.theWorld.setSpawnLocation();
        this.theWorld.removeAllEntities();
        int i = 0;

        if (this.thePlayer != null)
        {
            i = this.thePlayer.getEntityId();
            this.theWorld.removeEntity(this.thePlayer);
        }

        this.renderViewEntity = null;
        this.thePlayer = this.playerController.createClientPlayer(this.theWorld,
                this.thePlayer == null ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
        this.thePlayer.dimension = dimension;
        this.renderViewEntity = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        this.theWorld.spawnEntityInWorld(this.thePlayer);
        this.playerController.flipPlayer(this.thePlayer);
        this.thePlayer.movementInput = new fr.alessdevita.valtherion.utils.MovementInputFromOptions(this.gameSettings);
        this.thePlayer.setEntityId(i);
        this.playerController.setPlayerCapabilities(this.thePlayer);

        if (this.currentScreen instanceof GuiGameOver)
        {
            this.displayGuiScreen((Gui) null);
        }
    }

    /**
     * Demo ou non.
     */
    public final boolean isDemo()
    {
        return this.isDemo;
    }

    public NetHandlerPlayClient getNetHandler()
    {
        return this.thePlayer != null ? this.thePlayer.sendQueue : null;
    }

    public boolean isSingleplayer() { return this.theIntegratedServer != null; }
    public boolean isJava64bit() { return this.jvm64bit; }
    public EntityClientPlayerMP getPlayer() { return this.thePlayer; }
    public GameClient getGameClient() { return this.thePlayer == null ? null : this.thePlayer.getGameClient(); }
    public FontRenderer getFontRenderer() { return this.fontRenderer; }
    public void openMainMenu() { this.displayGuiScreen(new GuiMainMenu()); }
    public void openOptionsMenu() { this.displayGuiScreen(new fr.alessdevita.valtherion.client.gui.GuiOptions(this.currentScreen instanceof GuiScreen ? (GuiScreen)this.currentScreen : null)); }
    public void closeIngameMenu() { this.displayGuiScreen(null); this.setIngameFocus(); }
    public void closeEnchantmentGui() { this.displayGuiScreen(null); }
    public void closeMerchantGui() { this.displayGuiScreen(null); }
    public void closeExtendedGui() { this.displayGuiScreen(null); }
    public void updateLanguage() { if (this.gameSettings != null) this.gameSettings.saveOptions(); }
    public void saveGame() {
        if (this.currentWorldInfo != null && this.currentWorldFolder != null && this.saveLoader != null) {
            this.saveLoader.getSaveLoader(this.currentWorldFolder, false).saveWorldInfo(this.currentWorldInfo);
        }
    }
    public void freeUnusedResources() { System.gc(); }
    public void stop() { this.running = false; if (this.window != 0L) org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose(this.window, true); }
    public void restartGame() { if (this.thePlayer != null) this.thePlayer.preparePlayerToSpawn(); this.displayGuiScreen(null); }
    public void setServerData(ServerData data) { this.currentServerData = data; }
    public void connectToServer(String host, int port) {
        if (host == null || host.isBlank() || port < 1 || port > 65535) throw new IllegalArgumentException("Adresse de serveur invalide");
        this.serverName = host;
        this.serverPort = port;
        this.displayGuiScreen(new GuiConnecting());
    }
    public void cancelTerrainDownload() { this.loadWorld((WorldClient)null); this.openMainMenu(); }
    public void reconnect() { if (this.serverName != null) connectToServer(this.serverName, this.serverPort); }
    public void createWorld(String name, fr.alessdevita.valtherion.world.WorldType type,
                            String seed, fr.alessdevita.valtherion.client.gui.GuiCreateWorld.GameMode mode) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Le monde doit avoir un nom");
        long worldSeed = 0L;
        if (seed != null && !seed.isBlank()) {
            try { worldSeed = Long.parseLong(seed); } catch (NumberFormatException ignored) { worldSeed = seed.hashCode(); }
        }
        launchIntegratedServer(name.trim(), name.trim(), new fr.alessdevita.valtherion.world.WorldSettings(
                worldSeed, type, true, mode == fr.alessdevita.valtherion.client.gui.GuiCreateWorld.GameMode.HARDCORE));
    }

    public static boolean isGuiEnabled()
    {
        return theValtherion == null || !theValtherion.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled()
    {
        return theValtherion != null && theValtherion.gameSettings.fancyGraphics;
    }

    /**
     * Occlusion ambiante activee ou non.
     */
    public static boolean isAmbientOcclusionEnabled()
    {
        return theValtherion != null && theValtherion.gameSettings.ambientOcclusion != 0;
    }

    /**
     * Pick block (clic molette) : selectionne le bloc vise.
     * TODO: pick block sur entites une fois les entites Valtherion livrees.
     */
    private void pickerBlock()
    {
        // TODO: reimplementer avec l'API Block/Item de Valtherion
    }

    /**
     * Ajoute les infos GL/monde au rapport de crash.
     */
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport crashReportIn)
    {
        final Valtherion client = this;
        crashReportIn.getCategory().addCrashSectionCallable("Launched Version", new Callable()
        {
            public String call()
            {
                return client.launchedVersion;
            }
        });
        crashReportIn.getCategory().addCrashSectionCallable("LWJGL", new Callable()
        {
            public String call()
            {
                return org.lwjgl.Version.getVersion();
            }
        });
        crashReportIn.getCategory().addCrashSectionCallable("OpenGL", new Callable()
        {
            public String call()
            {
                return org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER)
                        + " GL version " + org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_VERSION)
                        + ", " + org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_VENDOR);
            }
        });
        crashReportIn.getCategory().addCrashSectionCallable("Is Modded", new Callable()
        {
            public String call()
            {
                String s = ClientBrandRetriever.getClientModName();
                return !s.equals("vanilla") ? "Definitely; Client brand changed to '" + s + "'" : "Probably not.";
            }
        });
        crashReportIn.getCategory().addCrashSectionCallable("Type", new Callable()
        {
            public String call()
            {
                return "Client (map_client.txt)";
            }
        });

        if (this.theWorld != null)
        {
            this.theWorld.addWorldInfoToCrashReport(crashReportIn);
        }

        return crashReportIn;
    }

    /**
     * Retourne l'instance singleton du client.
     */
    public static Valtherion getValtherion()
    {
        return theValtherion;
    }

    public void scheduleResourcesRefresh()
    {
        this.refreshTexturePacksScheduled = true;
    }

    /**
     * Retourne l'instance de PlayerUsageSnooper.
     */
    public PlayerUsageSnooper getPlayerUsageSnooper()
    {
        return this.usageSnooper;
    }

    /**
     * Temps systeme en millisecondes.
     * NB : LWJGL3 n'a plus Sys ; on utilise System.currentTimeMillis().
     */
    public static long getSystemTime()
    {
        return System.currentTimeMillis();
    }

    /**
     * Plein ecran ou non.
     */
    public boolean isFullScreen()
    {
        return this.fullscreen;
    }

    public Session getSession()
    {
        return this.session;
    }

    public Multimap getUserProperties()
    {
        return this.userProperties;
    }

    public Proxy getProxy()
    {
        return this.proxy;
    }

    public TextureManager getTextureManager()
    {
        return this.renderEngine;
    }

    public ISaveFormat getSaveFormat() { return this.saveLoader; }

    /** Retourne le handle GLFW de la fenêtre, notamment pour lire la souris depuis un écran. */
    public long getWindowHandle()
    {
        return this.window;
    }

    public ResourcePackRepository getResourcePackRepository()
    {
        return this.mcResourcePackRepository;
    }

    public SkinManager getSkinManager()
    {
        return this.skinManager;
    }

    /**
     * Planifie un Callable sur le thread client (file consommee dans runTick).
     */
    public ListenableFuture callFromClientThread(Callable callable)
    {
        java.util.Objects.requireNonNull(callable, "callable");

        if (!this.isClientThread())
        {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);

            synchronized (this.scheduledTasks)
            {
                this.scheduledTasks.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
        {
            try
            {
                return Futures.immediateFuture(callable.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedFuture(exception);
            }
        }
    }

    public ListenableFuture addScheduledTask(Runnable runnable)
    {
        java.util.Objects.requireNonNull(runnable, "runnable");
        return this.callFromClientThread(Executors.callable(runnable));
    }

    public boolean isClientThread()
    {
        return Thread.currentThread() == this.clientThread;
    }

    /**
     * Helper souris/entrees GLFW : bufferise les evenements des callbacks
     * et les expose via nextMouseEvent()/nextKeyboardEvent() pour conserver
     * l'architecture Mouse.next()/Keyboard.next() de la boucle de tick.
     */
    public static class MouseHelper
    {
        private final long window;
        private final Queue<MouseEvent> mouseEvents = new java.util.ArrayDeque<MouseEvent>();
        private final Queue<KeyboardEvent> keyboardEvents = new java.util.ArrayDeque<KeyboardEvent>();
        private MouseEvent currentMouseEvent;
        private KeyboardEvent currentKeyboardEvent;

        public MouseHelper(long windowIn)
        {
            this.window = windowIn;
        }

        private synchronized void addMouseButtonEvent(int button, int action)
        {
            if (action == GLFW_PRESS || action == GLFW_RELEASE)
            {
                this.mouseEvents.add(new MouseEvent(button, action == GLFW_PRESS, 0));
            }
        }

        private synchronized void addScrollEvent(int amount)
        {
            if (amount != 0)
            {
                this.mouseEvents.add(new MouseEvent(-1, false, amount));
            }
        }

        private synchronized void addKeyboardEvent(int key, int action)
        {
            if (action == GLFW_PRESS || action == GLFW_RELEASE || action == GLFW_REPEAT)
            {
                this.keyboardEvents.add(new KeyboardEvent(key, action != GLFW_RELEASE));
            }
        }

        public void grabMouseCursor()
        {
            glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }

        public void ungrabMouseCursor()
        {
            glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }

        public synchronized boolean nextMouseEvent()
        {
            this.currentMouseEvent = this.mouseEvents.poll();
            return this.currentMouseEvent != null;
        }

        public synchronized boolean nextKeyboardEvent()
        {
            this.currentKeyboardEvent = this.keyboardEvents.poll();
            return this.currentKeyboardEvent != null;
        }

        public int getEventButton()
        {
            return this.currentMouseEvent == null ? -1 : this.currentMouseEvent.button;
        }

        public boolean getEventButtonState()
        {
            return this.currentMouseEvent != null && this.currentMouseEvent.state;
        }

        public int getEventDWheel()
        {
            return this.currentMouseEvent == null ? 0 : this.currentMouseEvent.wheel;
        }

        public int getEventKey()
        {
            return this.currentKeyboardEvent == null ? GLFW_KEY_UNKNOWN : this.currentKeyboardEvent.key;
        }

        public boolean getEventKeyState()
        {
            return this.currentKeyboardEvent != null && this.currentKeyboardEvent.state;
        }

        private static final class MouseEvent
        {
            private final int button;
            private final boolean state;
            private final int wheel;

            private MouseEvent(int button, boolean state, int wheel)
            {
                this.button = button;
                this.state = state;
                this.wheel = wheel;
            }
        }

        private static final class KeyboardEvent
        {
            private final int key;
            private final boolean state;

            private KeyboardEvent(int key, boolean state)
            {
                this.key = key;
                this.state = state;
            }
        }
    }
}
