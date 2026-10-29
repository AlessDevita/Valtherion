package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.client.renderer.texture.AbstractTexture;
import fr.alessdevita.valtherion.client.renderer.texture.ITickableTextureObject;
import fr.alessdevita.valtherion.client.renderer.texture.TextureUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

/** Télécharge une image hors du thread OpenGL puis l'envoie au GPU au tick suivant. */
public class ThreadDownloadImageData extends AbstractTexture implements ITickableTextureObject {
    private final URL imageUrl;
    private final IImageBuffer imageBuffer;
    private final AtomicBoolean downloading = new AtomicBoolean();
    private volatile BufferedImage downloadedImage;
    private volatile boolean failed;

    public ThreadDownloadImageData(URL imageUrl, IImageBuffer imageBuffer) {
        this.imageUrl = imageUrl;
        this.imageBuffer = imageBuffer;
    }

    @Override
    public void loadTexture() {
        if (this.imageUrl == null || !this.downloading.compareAndSet(false, true)) return;
        Thread worker = new Thread(this::download, "valtherion-image-download");
        worker.setDaemon(true);
        worker.start();
    }

    private void download() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) this.imageUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "Valtherion/1.0");
            try (java.io.InputStream stream = connection.getInputStream()) {
                BufferedImage image = ImageIO.read(stream);
                if (image == null) throw new IOException("Format d'image non reconnu.");
                if (this.imageBuffer != null) image = this.imageBuffer.parseUserSkin(image);
                this.downloadedImage = image;
            }
        } catch (IOException | RuntimeException exception) {
            this.failed = true;
            System.err.println("Téléchargement d'image impossible : " + exception.getMessage());
        } finally {
            if (connection != null) connection.disconnect();
            this.downloading.set(false);
        }
    }

    @Override
    public void tick() {
        BufferedImage image = this.downloadedImage;
        if (image == null) return;
        this.downloadedImage = null;
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), image, true, true);
        setLoaded(true);
        if (this.imageBuffer != null) this.imageBuffer.skinAvailable();
    }

    public boolean isDownloading() { return this.downloading.get(); }
    public boolean hasFailed() { return this.failed; }
    public BufferedImage getDownloadedImage() { return this.downloadedImage; }
}
