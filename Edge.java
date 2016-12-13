import java.util.*;
import java.lang.Math;

/**
  * Luokka solmut yhdistäville kaarille.
  * <p>
  * Tietorakenteet 2015, harjoitustyö
  * <p>
  * @author Sami-Santeri Svensk (Svensk.Sami-Santeri.O@student.uta.fi)
  */

public class Edge {

	private int pituus;
	private int indeksi;
	private Vertex from;
	private Vertex to;
	private boolean kulku;

	public Edge (Vertex f, Vertex t, int i) {
		from(f);
		to(t);
		indeksi(i);
		pituus(f, t);
	}

	public void pituus(Vertex f, Vertex t) {
		int erotusX = f.koordinaattiX() - t.koordinaattiX();
		double potenssiX = Math.pow(erotusX, 2);
		int erotusY = f.koordinaattiY() - t.koordinaattiY();
		double potenssiY = Math.pow(erotusY, 2);
		pituus = (int)Math.sqrt(potenssiX + potenssiY);
	}
	public int pituus() {
		return pituus;
	}
	public void indeksi(int i) {
		indeksi = i;
	}
	public int indeksi() {
		return indeksi;
	}
	public void from(Vertex f) {
		from = f;
	}
	public Vertex from() {
		return from;
	}
	public void to(Vertex t) {
		to = t;
	}
	public Vertex to() {
		return to;
	}

	public void kulje() {
		kulku = true;
	}
	public void poistaKulku() {
		kulku = false;
	}

	public boolean kulku() {
		return kulku;
	}

	public boolean equals(Edge e) {
		if (indeksi == e.indeksi) {
			return true;
		}
		else
			return false;
	}

	public String toString() {
		final char SPACE = ' ';
		String mjono = "Kaari" + SPACE + indeksi + SPACE + "kulkee solmusta" + SPACE + from.indeksi() + SPACE + "solmuun" + SPACE + to.indeksi() + SPACE + ", pituus on" + SPACE + pituus;
		return mjono;
	}
}