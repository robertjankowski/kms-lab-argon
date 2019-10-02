package pl.kms.argon.atom;

public class Atom {

    public int pos;
    public double x, y, z;    // positions
    public double px, py, pz; // momentum
    public double Fx, Fy, Fz; // forces

    public Atom() {}

    public Atom(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Atom mult(double a) {
        return new Atom(a * x, a * y, a * z);
    }

    public double abs() {
        return Math.sqrt(x * x + y * y + z * z);
    }

	public String positionsToString() {
        return x + "\t" + y + "\t" + z + "\n";
    }

    public String momentumToString() {
    	return px + "\t" + py + "\t" + pz + "\n";
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
				"x=" + x +
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
