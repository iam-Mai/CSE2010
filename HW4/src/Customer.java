/*

  Author: Sasithorn Hannarong
  Email: shannarong2015@my.fit.deu
  Course: CSE2010
  Section: 03
  Description: HW4.java 
  Used with HW4.java

*/

public class Customer {
//s is a number of songs that both customers have rate
    private double s = 0;
//distance get from distance(x, y) = 1/|S| + (1/|S| * sum of |rating x(s) − rating y(s)|)
    private double distance = 0;
//zigma is sum of |rating x(s) − rating y(s)|
    private double zigma = 0;
    
    public double getS() {
        return s;
    }
    
    public double getD() {
        return distance;
    }
    
    public double getZ() {
        return zigma;
    }
    
    public void setS(double ns) {
        this.s = ns;
    }
    
    public void setD(double d) {
        this.distance = d;
    }
    
    public void setZ(double z) {
        this.zigma = z;
    }
}
