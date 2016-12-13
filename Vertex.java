import java.util.*;

/**
  * Luokka solmuille
  * <p>
  * Tietorakenteet 2015, harjoitustyö
  * <p>
  * @author Sami-Santeri Svensk (Svensk.Sami-Santeri.O@student.uta.fi)
  */

public class Vertex {

	private int koordinaattiX;
	private int koordinaattiY;
	private int indeksi;
	private boolean vierailu = false;
	private boolean puussa = false;
	private Object alkio;
	private ArrayList<Edge> sisaan = new ArrayList<Edge>();
    private ArrayList<Edge> ulos = new ArrayList<Edge>();
    private ArrayList<Edge> kaikki = new ArrayList<Edge>();
	
	public Vertex(int x, int y, int i) {
		koordinaattiX(x);
		koordinaattiY(y);
		indeksi(i);
	}

	public void sisaan(Edge e) {
		sisaan.add(e);
		kaikki.add(e);
	}

	public ArrayList<Edge> sisaan() {
		return sisaan;
	}

	public void ulos(Edge e) {
		ulos.add(e);
		kaikki.add(e);
	}

	public ArrayList<Edge> ulos() {
		return ulos;
	}

	public ArrayList<Edge> kaikki() {
		return kaikki;
	}

	public void koordinaattiX(int x) {
	    koordinaattiX = x;
	}

	public int koordinaattiX() {
		return koordinaattiX;
	}
	public void koordinaattiY(int y) {
		koordinaattiY = y;
	}

	public int koordinaattiY() {
		return koordinaattiY;
	}	
	public void indeksi(int i) {
		indeksi = i;
	}

	public int indeksi() {
		return indeksi;
	}
	public void alkio(Object a) {
		alkio = a;
	}
	public Object alkio() {
		return alkio;
	}

	public void vieraile() {
		vierailu = true;
	}
	public void unohdaVierailu() {
		vierailu = false;
	}
	public boolean vierailtu() {
		return vierailu;
	}

	public void lisaaPuuhun() {
		puussa = true;
	}
	public void poistaPuusta() {
		puussa = false;
	}
	public boolean puussa() {
		return puussa;
	}

	//Jos indeksit ovat samat, solmut ovat samat.
	public boolean equals(Vertex v) {
		if(indeksi == v.indeksi) {
			return true;
		}
		else
			return false;
	}

	public String toString() {
		final char SPACE = ' ';
		String mjono = "Solmun" + SPACE + indeksi + SPACE + "koordinaatit ovat" + SPACE + koordinaattiX + "," + koordinaattiY;
		return mjono;
	}

}