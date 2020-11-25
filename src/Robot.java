//George Anumba

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class Robot extends Energy {
    private ArrayList<Energy> energies;
    private ArrayDeque<Energy> memory = new ArrayDeque<>();
    private final double shortestMove = 13;
    private final double longestMove = 18.38;
    private final int snapDistance = 9;
    private double robotMaxEnergy = 400;
    private double distanceTraveled;
    private boolean isCurious = false;
    private boolean isHungry = false;
    private boolean isInactive = false;
    private int robotDetectionRadius = 13;
    private boolean isQueue;
    Point hungryGoal;
    Point curiousGoal;


    public double getDistanceTraveled() {
        return distanceTraveled;
    }
    public String toString() {

//        String format=String.format("",);
        return (
                (isHungry ? "Hungry " : "Curious") +
                        " Distance=" + Math.round(distanceTraveled) +
                        " Location=[" + x + "," + y + "]" +
                        (curiousGoal != null ? " CuriousGoal=[" + curiousGoal.x + "," + curiousGoal.y + "]" : "") +
                        (hungryGoal != null ? " HungryGoal=[" + hungryGoal.x + "," + hungryGoal.y + "]" : "") +
                        " Energy Level=" + Math.round(amount) +
                        " Memory=" + memory
        );
    }


    public Robot(ArrayList<Energy> energiesInput, boolean isQueue) {
        super(400);
        this.isQueue = isQueue;
        this.setLocation(0, 0);
        distanceTraveled = 0;
        energies = energiesInput;
    }

    public int status() {
        int status;
//        while (!isInactive){
        if (amount > robotMaxEnergy / 2) {
            isCurious = true;
            isHungry = false;
            isInactive = false;
            status = CHR.CURIOUS;
        }
        else if (amount > 0 && amount <= robotMaxEnergy / 2) {
            isCurious = false;
            isHungry = true;
            isInactive = false;
            status = CHR.HUNGRY;
        }
        else {
            isCurious = false;
            isHungry = false;
            isInactive = true;
            status = CHR.INACTIVE;
        }
//        }
        return status;
    }


    public void curious() {
//        System.out.println("Is Curious");
        //Curious Movement
        hungryGoal = null;
        if (curiousGoal == null) {
            curiousGoal = getCuriousGoal();
        }

        moveTowards(curiousGoal);
        if ((this.getLocation().equals(curiousGoal.getLocation()))) {
            curiousGoal = getCuriousGoal();
        }

    }


    public void hungry() {
        curiousGoal = null;
//        System.out.println("Is Hungry");

        if (hungryGoal == null) {
            hungryGoal = getHungryGoal();
        }
        moveTowards(hungryGoal);
        if ((this.getLocation().equals(hungryGoal.getLocation()))) {
            hungryGoal = getHungryGoal();
            // Takes the energy from the point
            if (!memory.isEmpty()) {
                Energy e;
                if (isQueue) {
                    e = memory.removeLast();
                }
                else {
                    e = memory.removeFirst();
                }
                if (e.amount <= (robotMaxEnergy - this.amount)) {
                    this.amount += e.amount;
                    e.amount = 0;
                }
                else {
                    double req = robotMaxEnergy - this.amount;
                    e.amount -= req;
                    this.amount += req;
                }
            }

        }

    }


    public void scan() {
        for (Energy e : energies) {
            if (e.distance(this.x, this.y) <= robotDetectionRadius && !(memory.contains(e))) {
                memory.addFirst(e);

                if (isHungry) {
                    if (e.getLocation().equals(this.getLocation())) {


                    }
                }
            }
        }
//        System.out.println(memory);
    }

    private Point getRandomPoint() {
        Point o = new Point(0, 0);
        Point p = new Point(400, 400);
        Random random = new Random();
        while (o.distance(p) > 200) {
            int x = (random.nextInt(400)) - 200;
            int y = (random.nextInt(400)) - 200;
            p.setLocation(x, y);
        }
        return p;
    }

    private void incrementCapacity(double x) {
        amount -= x;
        distanceTraveled += x;
    }

    private void snapToGoal(Point point) {
        this.setLocation(point.x, point.y);

    }

    private Point getCuriousGoal() {
        Point curiousGoal;
        curiousGoal = getRandomPoint();
        return curiousGoal;
    }

    private Point getHungryGoal() {
        Point hungryGoal;
        if (!(memory.isEmpty())) {
            if (isQueue) {
                hungryGoal = memory.getLast();
            }
            else {
                hungryGoal = memory.getFirst();
            }

            return hungryGoal;
        }
        else return getCuriousGoal();
    }

    private void moveTowards(Point goal) {
        if (goal.x == this.x || goal.y == this.y) {
            //Moves Vertically
            if (goal.x == this.x) {
                if (goal.distance(this.x, this.y) > snapDistance) {
                    if (goal.y > this.y) {
                        this.setLocation(this.x, (this.y + shortestMove));
                        incrementCapacity(shortestMove);
                        scan();
                        status();
                    }
                    else if (goal.y < y) {
                        this.setLocation(this.x, (this.y - shortestMove));
                        incrementCapacity(shortestMove);
                        scan();
                        status();
                    }
                }
                else {
                    //Snaps to curious goal when within 9 units
                    snapToGoal(goal);
                }

            }
            //Moves Horizontally
            if (goal.y == this.y) {
                if (goal.distance(this.x, this.y) > snapDistance) {
                    if (goal.x > x) {
                        this.setLocation((this.x + shortestMove), this.y);
                        incrementCapacity(shortestMove);
                        scan();
                        status();
                    }
                    else if (goal.x < x) {
                        this.setLocation((this.x - shortestMove), this.y);
                        incrementCapacity(shortestMove);
                        scan();
                        status();
                    }
                }
                else {
                    snapToGoal(goal);
                }
            }
            //All the Possible Diagonal Moves
        }
        else if (goal.distance(this.x, this.y) > snapDistance) {

            //North East
            if (goal.x > this.x && goal.y > this.y) {
                this.setLocation((this.x + shortestMove), (this.y + shortestMove));
                incrementCapacity(longestMove);
                scan();
                status();

                //North West
            }
            else if (goal.x < this.x && goal.y < this.y) {
                this.setLocation((this.x - shortestMove), (this.y - shortestMove));
                incrementCapacity(longestMove);
                scan();
                status();

                //South East
            }
            else if (goal.x > this.x && goal.y < this.y) {
                this.setLocation((this.x + shortestMove), (this.y - shortestMove));
                incrementCapacity(longestMove);
                scan();
                status();

                //South West
            }
            else {
                this.setLocation((this.x - shortestMove), (this.y + shortestMove));
                incrementCapacity(longestMove);
                scan();
                status();
            }
        }
        else {
            snapToGoal(goal);

        }
    }
}
