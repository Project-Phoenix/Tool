/*
 * Copyright (C) 2013 Project-Phoenix
 * 
 * This file is part of Tool.
 * 
 * Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Tool.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.swtapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.filter.EduFilter;
import de.phoenix.filter.TextFilter;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.PhoenixClient;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;

public class DownloadHandler {
    public static Client client;
    public static String BASE_URL;
    private WebResource wrTask;
    private WebResource wrSheet;

    public DownloadHandler() {
        client = PhoenixClient.create();
        BASE_URL = "http://meldanor.dyndns.org:8080/PhoenixWebService/rest";
        wrSheet = PhoenixTaskSheet.getResource(client, BASE_URL);
    }

    public List<String> showTasks(PhoenixTaskSheet taskSheet) {

        List<String> titles = new ArrayList<String>();
        List<PhoenixTask> taskTitles = taskSheet.getTasks();
        for (int i = 0; i < taskTitles.size(); i++) {
            titles.add(i, taskTitles.get(i).getTitle());
        }
        return titles;
    }

    public List<PhoenixTaskSheet> showAllTaskSheets(Shell shell) {

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(client, BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        if (response.getStatus() == 404) {
            MessageBox dbDown= new MessageBox(shell);
            dbDown.setMessage("Server not available. Please try again later!");
            dbDown.open();           
            
        }

        List<PhoenixTaskSheet> sheets = EntityUtil.extractEntityList(response);

        return sheets;
    }

    public void getDownloadData() {

    }

    public void createTaskOnComputer(File file, PhoenixTaskSheet taskSheet, String path, String taskTitle) throws IOException {
        wrTask = PhoenixTask.getResource(client, BASE_URL);
        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", taskTitle);
        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        PhoenixTask reqTitle = EntityUtil.extractEntity(post);

        List<PhoenixText> pattern = reqTitle.getPattern();
        List<PhoenixAttachment> attachment = reqTitle.getAttachments();
        String description = reqTitle.getDescription();

        TextFilter t = EduFilter.INSTANCE;
        String descrFiltered = t.filter(description);

        File directory = new File(path, taskSheet.getTitle());
        directory.mkdir();

        // Task is without any given code or attachment, just text
        if (pattern.isEmpty() && attachment.isEmpty()) {

            File taskFile = new File(directory, taskTitle + ".java");
            // If file doesn't exist, create this file and write the
            // description as a comment in it
            writeInFile(taskFile, "/*" + descrFiltered + "*/");

            // Task has one pattern hence one class to submit, no
            // attachments
        } else if (pattern.size() == 1 && attachment.isEmpty()) {

            File taskFile = new File(directory, pattern.get(0).getFullname());

            writeInFile(taskFile, "/*" + descrFiltered + "*/\n" + pattern.get(0).getText());

            // Task has at least two patterns or some attachments
        } else {

            File dir = new File(directory, taskTitle);

            dir.mkdir();

            if (!pattern.isEmpty()) {
                // Writes each class in a file in the directory
                for (PhoenixText clazz : pattern) {
                    File taskFile = new File(directory + "/" + taskTitle, clazz.getFullname());
                    writeInFile(taskFile, "/*" + description + "*/\n" + clazz.getText());

                }
            }

            if (!attachment.isEmpty()) {

                if (pattern.isEmpty()) {
                    File taskFile = new File(directory + "/" + taskTitle, taskTitle + ".java");
                    writeInFile(taskFile, descrFiltered);
                }

                for (PhoenixAttachment picture : attachment) {

                    File taskFile = new File(directory + "/" + taskTitle, picture.getFullname());
                    byte[] content = picture.getContent();
                    FileOutputStream fos;

                    fos = new FileOutputStream(taskFile);
                    fos.write(content);
                    fos.close();

                }
            }
        }

    }

    public void writeInFile(File file, String text) {
        Writer fw;
        Writer bw;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadChosenTaskSheet(String path, String taskSheetTitle, File file) throws IOException {

        PhoenixTaskSheet wantedTaskSheet = titleToTaskSheet(taskSheetTitle);

        // All tasks from selected PhoenixTaskSheet
        List<PhoenixTask> tasks = wantedTaskSheet.getTasks();

        // Parent folder
        File fileX = new File(path, taskSheetTitle);

        file.mkdir();

        for (int i = 0; i < tasks.size(); i++) {

            String title = tasks.get(i).getTitle();
            createTaskOnComputer(fileX, wantedTaskSheet, path, title);
        }

    }

    public void downloadChosenTask(String path, PhoenixTaskSheet taskSheet, String taskTitle, File file) throws IOException {

        createTaskOnComputer(file, taskSheet, path, taskTitle);

    }

    public PhoenixTaskSheet titleToTaskSheet(String title) {

        SelectEntity<PhoenixTaskSheet> selectByTitle = new SelectEntity<PhoenixTaskSheet>().addKey("title", title);
        ClientResponse post = wrSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        PhoenixTaskSheet taskByTitle = EntityUtil.extractEntity(post);

        return taskByTitle;

    }

    public String writeTaskSheetTo(PhoenixTaskSheet taskSheet) throws IOException {

        // Create a temporary folder in the temp directory
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), taskSheet.getTitle());
        // Delete the older folder, if it exists
        if (tmpDir.exists())
            deleteDirectory(tmpDir);

        // Create Folder
        tmpDir.mkdir();

        // Write the task and its related attachments, pattern and description
        List<PhoenixTask> tasks = taskSheet.getTasks();
        for (PhoenixTask phoenixTask : tasks) {
            File taskDir = new File(tmpDir, phoenixTask.getTitle());
            taskDir.mkdir();

            PrintWriter writer = new PrintWriter(new File(taskDir, "Aufgabe.html"), "UTF-8");
            writer.write(phoenixTask.getDescription());
            writer.close();

            for (PhoenixAttachment attachment : phoenixTask.getAttachments()) {
                attachment.writeToFile(new File(taskDir, attachment.getFullname()));
            }
            for (PhoenixText text : phoenixTask.getPattern()) {
                text.writeToFile(new File(taskDir, text.getFullname()));
            }
        }

        return tmpDir.getAbsolutePath();
    }

    public String writeTask(PhoenixTask task) throws IOException {

        // Create a temporary folder in the temp directory
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), task.getTitle());

        // Delete the older folder, if it exists
        if (tmpDir.exists())
            deleteDirectory(tmpDir);

        // Create folder
        tmpDir.mkdir();

        // Write the task and its related attachments, pattern and description

        File taskDir = new File(tmpDir, task.getTitle());
        taskDir.mkdir();

        PrintWriter writer = new PrintWriter(new File(taskDir, "Aufgabe.html"), "UTF-8");
        writer.write(task.getDescription());
        writer.close();

        for (PhoenixAttachment attachment : task.getAttachments()) {
            attachment.writeToFile(new File(taskDir, attachment.getFullname()));
        }
        for (PhoenixText text : task.getPattern()) {
            text.writeToFile(new File(taskDir, text.getFullname()));
        }

        return tmpDir.getAbsolutePath();
    }
    // deleting a folder recursively
    private void deleteDirectory(File dir) {

        File[] subFiles = dir.listFiles();
        for (int i = 0; i < subFiles.length; i++) {
            File file = subFiles[i];
            if (file.isDirectory())
                deleteDirectory(file);
            else
                file.delete();
        }
        dir.delete();
    }

}
