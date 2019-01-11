/*
  Author: Sasithorn Hannarong
  Email: shannarong2015@my.fit.deu
  Course: CSE2010
  Section: 03
  Description: Vertice.java store information of each cell in the grid
*/
public class Vertice {
    public char element;
    public int x = 0, y = 0;
    public Vertice parent = null;
    public int distance = 0;
    public Vertice() {
        this.parent = null;
    }
    public Vertice(int ix, int iy) {
        this.x = ix;
        this.y = iy;
        this.parent = null;
    }
    public Vertice(char c, int ix, int iy) {
        this.x = ix;
        this.y = iy;
        this.element = c;
    }
    
    public Vertice(int ix, int iy, Vertice p, int d ) {
        this.x = ix;
        this.y = iy;
        this.parent = p;
        this.distance = d;
    }
    
    public Vertice getParent() {
        return parent;
    }
}
