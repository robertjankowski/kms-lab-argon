package pl.kms.argon.atom;

public class Atom {

    public double x, y, z;    // positions
    public double px, py, pz; // momentum
    public double Fx, Fy, Fz; // forces

    public Atom() {
    }

    public void multPos(double a) {
        setPosition(a * x, a * y, a * z);
    }

    public double absPosition() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double absMomentum() {
        return Math.sqrt(px * px + py * py + pz * pz);
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setMomentum(double px, double py, double pz) {
        this.px = px;
        this.py = py;
        this.pz = pz;
    }
}
