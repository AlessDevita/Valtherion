package fr.alessdevita.valtherion.client.shader;

/**
 * Uniform « par défaut » : no-op utilisé quand un uniform GLSL n'existe pas
 * ou n'a pas été trouvé dans le programme. Toutes les méthodes sont des no-ops
 * car la location est -1 (non valide).
 * <p>
 * Étend {@link ShaderUniform} avec un programme 0 et un nom vide :
 * {@link ShaderUniform#isValid()} retournera {@code false}.
 */
public final class ShaderDefault extends ShaderUniform {
    private static final ShaderDefault INSTANCE = new ShaderDefault();

    public static ShaderDefault get() {
        return INSTANCE;
    }

    private ShaderDefault() {
        super(0, "", -1);
    }

    @Override
    public void set(float x) { }
    @Override
    public void set(float x, float y) { }
    @Override
    public void set(float x, float y, float z) { }
    @Override
    public void set(float x, float y, float z, float w) { }

    @Override
    public void set(int x) { }
    @Override
    public void set(int x, int y) { }
    @Override
    public void set(int x, int y, int z) { }
    @Override
    public void set(int x, int y, int z, int w) { }

    @Override
    public void set(float[] values) { }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String toString() {
        return "ShaderDefault[no-op]";
    }
}
