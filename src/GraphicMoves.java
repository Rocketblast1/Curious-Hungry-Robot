
public class GraphicMoves {

    public static void main(String[] args) {
        String type = args[0];
        boolean isQueue = type.toLowerCase().equals("queue");
        CHR chr = new CHR(isQueue);
        chr.createEneryPoints();
        DrawPanel panel = new DrawPanel();
        panel.drawPanel(chr);
        int status = chr.robotStatus();
        while (status != CHR.INACTIVE) {
            if (status == CHR.CURIOUS) {
                chr.makeCuriousMove();
            }
            else {
                chr.makeHungryMove();
            }
            status = chr.robotStatus();
            chr.printStatus();
            panel.drawMovements();
            GraphicMoves.delay();

        }
    }
    public static void delay() {

        try {
            Thread.sleep( 650 );
        }
        catch ( InterruptedException interrupt ) { }
    }
}
