package ds.project1task2;
/*
 *
 * This file is the Model component of the MVC, and it models the business
 * logic for the web application. The class has a variable treemap for recording all results.
 * The logic involves an addAnswer to record when a new choice is made and a clearAnswer to
 * clear all answers when getResults is called.
 *
 */


import java.util.TreeMap;

public class ClickerModel {
    // TreeMap variable for storing results
    private TreeMap<String, Integer> m = new TreeMap<String, Integer>();

    /**
     * This method updates the number record of the choice map.
     * It adds 1 to the value of the choice key.
     *
     * @param c The choice need to update the treemap.
     */
    public void addAnswer(String c) {
        if (m.containsKey(c)) {
            m.put(c, m.get(c) + 1);
        } else {
            m.put(c, 1);
        }
    }

    /**
     * This method clears all results in the treemap.
     */
    public void clearAnswer() {
        m.clear();
    }

    /**
     * This method is the getter for the TreeMap variable.
     */
    public TreeMap<String, Integer> getMap() {
        return m;
    }


}
