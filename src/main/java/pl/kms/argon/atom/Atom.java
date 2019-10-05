package pl.kms.argon.atom;

public class Atom {

    public int pos;
    public double x, y, z;    // positions
    public double px, py, pz; // momentum
    public double Fx, Fy, Fz; // forces

    public Atom() {
    }

    public Atom(Atom atom) {
        this.x = atom.x;
        this.y = atom.y;
        this.px = atom.px;
        this.py = atom.py;
        this.pz = atom.pz;
        this.Fx = atom.Fx;
        this.Fy = atom.Fy;
        this.Fz = atom.Fz;
        this.pos = atom.pos;
    }

    public void multPos(double a) {
        setPosition(a * x, a * y, a * z);
    }

    public void setForces(double Fx, double Fy, double Fz) {
        this.Fx = Fx;
        this.Fy = Fy;
        this.Fz = Fz;
    }

    public double absPosition() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double absForce() {
        return Math.sqrt(Fx * Fx + Fy * Fy + Fz * Fz);
    }

    public double absPositionSubstraction(Atom atom) {
        double x = this.x - atom.x;
        double y = this.y - atom.y;
        double z = this.z - atom.z;
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void substractPosition(Atom atom) {
        setPosition(this.x - atom.x, this.y - atom.y, this.z - atom.z);
    }

    public String positionsToString() {
        return x + "\t" + y + "\t" + z + "\n";
    }

    public String momentumToString() {
        return px + "\t" + py + "\t" + pz + "\n";
    }

    public String forcesToString() {
        return Fx + "\t" + Fy + "\t" + Fz + "\n";
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

    @Override
    public String toString() {
        return "Atom{" +
                "pos=" + pos +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", px=" + px +
                ", py=" + py +
                ", pz=" + pz +
                ", Fx=" + Fx +
                ", Fy=" + Fy +
                ", Fz=" + Fz +
                '}';
    }
}
