package net.mindoth.shadowizardlib.client.particle.ember;

public class ParticleColor {

    public static ParticleColor defaultParticleColor() {
        return new ParticleColor(170, 25, 170);
    }

    private final float r;
    private final float g;
    private final float b;
    private final int color;

    public ParticleColor(int r, int g, int b) {
        this.r = r / 255F;
        this.g = g / 255F;
        this.b = b / 255F;
        this.color = (r << 16) | (g << 8) | b;
    }

    public ParticleColor(double red, double green, double blue) {
        this((int)red,(int) green,(int) blue);
    }

    public ParticleColor(float r, float g, float b){
        this((int)r,(int) g,(int) b);
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public int getColor() {
        return color;
    }

    public String serialize() {
        return "" + this.r + "," + this.g +","+this.b;
    }

    public static ParticleColor deserialize(String string) {
        if ( string == null || string.isEmpty() )
            return defaultParticleColor();
        String[] arr = string.split(",");
        return new ParticleColor(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()));
    }

    public static class IntWrapper {
        public int r;
        public int g;
        public int b;

        public IntWrapper(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public IntWrapper(ParticleColor color) {
            this.r = (int) (color.getRed() * 255.0);
            this.g = (int) (color.getGreen() * 255.0);
            this.b = (int) (color.getBlue() * 255.0);
        }
    }
}
