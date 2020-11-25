import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<Energy> points;
    private Robot robot;
    private Graphics2D g2;
    private List<Point> lastPoint=new ArrayList<>();

    public DrawPanel() {
        JFrame frame = new JFrame();
        frame.add(DrawPanel.this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setLocation(400,30);
        frame.setVisible(true);
    }

    public void drawPanel(CHR chr) {
        this.points = chr.energyPointsList;
        this.robot=chr.robot;
        lastPoint.add(new Point(0,0));
        setBackground(Color.WHITE);
    }

    public void drawMovements(){
        lastPoint.add(new Point(robot.x,robot.y));
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.scale(1.5,1.5);
        g2.translate(300, 300);
        g2.setColor(Color.green);


        g2.drawOval(-200,-200,400,400);
        g2.setColor(Color.black);
        g2.drawLine(-200, 0, 200, 0);
        g2.drawLine(0, -200, 0, 200);
        g2.setColor(Color.ORANGE);
        g2.fillOval(robot.x, robot.y, 10, 10);

        if (robot.curiousGoal!=null) {
            g2.setColor(Color.MAGENTA.darker());
            g2.fillOval(robot.curiousGoal.x, robot.curiousGoal.y, 15, 15);
        }
        if (robot.hungryGoal!=null) {
            g2.setColor(Color.green);
            g2.fillOval(robot.hungryGoal.x, robot.hungryGoal.y, 15, 15);
        }
        drawEnergyPoints(g2);
        drawRobot(g2);

    }

    private void drawRobot(Graphics2D g2) {
        for (int i=0;i<lastPoint.size()-1;i++){
            Point p1=lastPoint.get(i);
            Point p2=lastPoint.get(i+1);

            g2.setColor(Color.lightGray);
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }
        repaint();
    }

    private void drawEnergyPoints(Graphics2D g2) {
        for (Point point : points) {
            g2.setColor(Color.red);
            double amount=((Energy)point).amount;
            if (amount>0) {
                if (amount< 300){
                    g2.setColor(Color.blue);
                }
                g2.fillOval(point.x, point.y, 8, 8);
                g2.setColor(Color.black);
                g2.drawString(""+Math.round(amount), point.x, point.y);
            }
        }
    }

}