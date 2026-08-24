package com.shivprakash.to_dolist;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaskTest {

    @Test
    public void testTaskName() {

        Task task = new Task(
                "Study Java",
                "High",
                "Study",
                "25/08/2026",
                "18:00",
                "",
                false
        );

        assertEquals("Study Java", task.getTaskName());

        task.setTaskName("Study Android");

        assertEquals("Study Android", task.getTaskName());
    }

    @Test
    public void testPriority() {

        Task task = new Task(
                "Finish project",
                "High",
                "Work",
                "25/08/2026",
                "20:00",
                "",
                false
        );

        assertEquals("High", task.getPriority());

        task.setPriority("Low");

        assertEquals("Low", task.getPriority());
    }

    @Test
    public void testCategory() {

        Task task = new Task(
                "Learn Docker",
                "Medium",
                "DevOps",
                "26/08/2026",
                "10:00",
                "",
                false
        );

        assertEquals("DevOps", task.getCategory());

        task.setCategory("Programming");

        assertEquals("Programming", task.getCategory());
    }

    @Test
    public void testDueDate() {

        Task task = new Task(
                "Project meeting",
                "High",
                "Work",
                "30/08/2026",
                "14:00",
                "",
                false
        );

        assertEquals("30/08/2026", task.getDueDate());

        task.setDueDate("31/08/2026");

        assertEquals("31/08/2026", task.getDueDate());
    }

    @Test
    public void testDueTime() {

        Task task = new Task(
                "Meeting",
                "Medium",
                "Work",
                "30/08/2026",
                "14:00",
                "",
                false
        );

        assertEquals("14:00", task.getDueTime());

        task.setDueTime("16:00");

        assertEquals("16:00", task.getDueTime());
    }

    @Test
    public void testCompleted() {

        Task task = new Task(
                "Complete Android project",
                "High",
                "University",
                "30/08/2026",
                "18:00",
                "",
                false
        );

        assertFalse(task.isCompleted());

        task.setCompleted(true);

        assertTrue(task.isCompleted());
    }
}
