//George Anumba

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CHR {
    public static int INACTIVE = 0;
    public static int HUNGRY = 1;
    public static int CURIOUS = 2;
    public int energyAmount = 300;
    public int numOfEnergyPoints = 33;
    public int energyIntervalDistance = 20;
    ArrayList<Energy> energyPointsList = new ArrayList<Energy>();
    Robot robot;

    public CHR(boolean isQueue) {
        robot = new Robot(energyPointsList, isQueue);
    }


    public void createEneryPoints() {
        Energy energyPoint = new Energy(energyAmount);
        energyPoint.setLocation(getRandomPoint());
        energyPointsList.add(energyPoint);
        for (int j = 1; j < numOfEnergyPoints; j++) {
            energyPoint = new Energy(energyAmount);
            energyPoint.setLocation(testWithin20());
            energyPointsList.add(energyPoint);
        }
//        System.out.println("Creates The Energy Instances");
    }

    private Point testWithin20() {
        boolean passed = false;
        Point rpt = getRandomPoint();
        while (!passed) {
            passed = true;
            rpt = getRandomPoint();
            for (Energy e : energyPointsList) {
                if (rpt.distance(e) <= energyIntervalDistance) {
                    passed = false;
                    break;
                }
            }
        }
        return rpt;
    }

    public static Point getRandomPoint() {
        Random random = new Random();
        Point o = new Point(0, 0);
        Point p = new Point((random.nextInt(400)) - 200, (random.nextInt(400)) - 200);
        while (o.distance(p) > 200) {
            p.setLocation((random.nextInt(400)) - 200, (random.nextInt(400)) - 200);
        }
        return p;
    }

    public void makeCuriousMove() {
        robot.curious();
    }

    public void makeHungryMove() {
        robot.hungry();
    }

    public int robotStatus() {
        return robot.status();
    }


    public double getDistanceTraveled() {
        return robot.getDistanceTraveled();
    }

    public void printStatus() {
        System.out.println(robot);
    }

    public static void main(String[] args) {
        ArrayList<Double> distances=new ArrayList<>();
        String type = args[0];
        int runs = Integer.parseInt(args[1]);
        boolean isQueue = type.toLowerCase().equals("queue");
        System.out.println("Memory type: " + (isQueue ? " Queue" : "Stack")+" for "+runs+" times");

        for (int i = 0; i < runs; i++) {

            CHR chr = new CHR(isQueue);
            chr.createEneryPoints();
//            new DrawPanel().drawPanel(chr);
            int status = chr.robotStatus();
            while (status != INACTIVE) {
                if (status == CURIOUS) {
                    chr.makeCuriousMove();
                }
                else {
                    chr.makeHungryMove();
                }
                status = chr.robotStatus();
//                chr.printStatus();
                
            }
            distances.add(chr.getDistanceTraveled());
        }

        System.out.println(distances);
        System.out.println("End of simulation");

    }



}
