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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.KeyReader;


public class UploadHandler {

    private FileExtensions fileExtension;
    private WebResource wrSubmit;
    private MyHandler myhandler;
    private DownloadHandler downloadHandler;
    private Shell shell;
    private Display display;
    private Shell shellL;
    private List<Button> blist = new ArrayList<Button>();;

    public UploadHandler(Shell shell) {
        this.fileExtension = new FileExtensions();
        this.myhandler = new MyHandler();
        this.downloadHandler = new DownloadHandler();
        this.shell = shell;
//        wrTask = PhoenixTaskSheet.getResource(SWT_App.client, SWT_App.BASE_URL);
        wrSubmit = PhoenixTask.submitResource(SWT_App.client, SWT_App.BASE_URL);
    }

    public void prepareUpload(Table table, Combo combo) {
        shellL = new Shell(display, SWT.ON_TOP | SWT.CLOSE);
        shellL.setSize(250, 100);

        myhandler.centerWindow(shellL);

//        Image loginicon = new Image(display, this.getClass().getResourceAsStream("/loginkey_50x50.png"));
//        shell.setImage(loginicon);

        GridLayout gridlayout = new GridLayout();
        gridlayout.numColumns = 1;
        shellL.setLayout(gridlayout);

        GridData gridpbar = new GridData();
        gridpbar.horizontalAlignment = GridData.FILL;
        gridpbar.verticalAlignment = GridData.FILL;

        GridData gridpbar2 = new GridData();
        gridpbar2.horizontalAlignment = GridData.FILL;
        gridpbar2.grabExcessHorizontalSpace = true;
        gridpbar2.verticalAlignment = 1;

        ProgressBar pbar = new ProgressBar(shellL, SWT.INDETERMINATE);
        pbar.setLayoutData(gridpbar);

        Button buttos = new Button(shellL, SWT.PUSH);
        buttos.setText("Cancel");
        buttos.setLayoutData(gridpbar2);
        buttos.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                shellL.close();

            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        shellL.setText("Uploading...");

        shellL.pack();
        shellL.open();
        pbar.setSelection(0);

        List<String> uploadFiles = new ArrayList<String>();
        final List<PhoenixTaskSheet> taskSheets = downloadHandler.showAllTaskSheets(shell);

        for (int i = 0; i < table.getItems().length; i++) {
            uploadFiles.add((String) table.getItem(i).getData());

        }
        int taskName = combo.getSelectionIndex();
        int j = 0;
        int k = 0;
        if (combo.getSelectionIndex() != -1) {

            outter : for (j = 0; j < taskSheets.size(); j++) {

                for (k = 0; k < taskSheets.get(j).getTasks().size(); k++) {
                    pbar.setSelection(pbar.getSelection() + 1);
                    if (combo.getItem(taskName).equals(taskSheets.get(j).getTasks().get(k).getTitle())) {
                        break outter;
                    }
                }
            }

            try {
                execute(taskSheets.get(j).getTasks().get(k), uploadFiles, table, pbar);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        shellL.close();

    }
    public void execute(PhoenixTask task, List<String> uploadFiles, Table table, ProgressBar pbar) throws Exception {

        List<File> attachmentFileList = new ArrayList<File>();
        List<File> textFileList = new ArrayList<File>();
        for (int i = 0; i < uploadFiles.size(); i++) {
            if (fileExtension.isTextFile(uploadFiles.get(i))) {
                File textFile = new File(uploadFiles.get(i));
                textFileList.add(textFile);
            } else {
                File attachFile = new File(uploadFiles.get(i));
                attachmentFileList.add(attachFile);
            }

        }

//        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", task.getTitle());

//        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
//        System.out.println("Title is " + task.getTitle());
//        System.out.println(post.getStatus());
//        List<PhoenixTask> list = EntityUtil.extractEntityList(post);
//        PhoenixTask reqTask = list.get(0);

        PhoenixSubmission sub = new PhoenixSubmission(attachmentFileList, textFileList);

        table.removeAll();
        for (Button but : blist) {
            but.dispose();
        }

        // connects a solution to a task

        ClientResponse post = wrSubmit.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(task, Arrays.asList(sub)));

        if (post.getStatus() != 200) {

            MessageBox clientResponse = new MessageBox(shell);
            clientResponse.setMessage("An error ocurred. Please try it again!");
            clientResponse.open();
        }

        PhoenixSubmissionResult result = post.getEntity(PhoenixSubmissionResult.class);
// TODO wenn nicht 200 alle result konstanten durchgehen und ueberpruefen. result.getStatus().
        switch (result.getStatus()) {
            case COMPILED : {
                pbar.setMaximum(pbar.getMaximum());
                MessageBox submissionCompiled = new MessageBox(shell);
                submissionCompiled.setMessage("Submission compiled. Accepted!");
                submissionCompiled.open();
                break;
            }
            case ERROR : {
                MessageBox submissionError = new MessageBox(shell);
                if (result.getStatusText().length() > 150) {
                    submissionError.setMessage("An error occurred! Please look at the data in the folder of the GUI for more information.");
                    submissionError.open();
                    File file = new File("Error.log");
                    downloadHandler.writeInFile(file, result.getStatusText());
                    break;
                } else {
                    submissionError.setMessage(result.getStatusText());
                    submissionError.open();
                    break;
                }

            }
            case MISSING_FILES : {
                MessageBox submissionMissingFiles = new MessageBox(shell);
                submissionMissingFiles.setMessage(result.getStatusText());
                submissionMissingFiles.open();
                break;
            }
            case OK : {
                pbar.setMaximum(pbar.getMaximum());
                MessageBox submissionOk = new MessageBox(shell);
                submissionOk.setMessage("Submission accepted!");
                submissionOk.open();
                break;
            }
            case SUBMITTED : {
                pbar.setMaximum(pbar.getMaximum());
                MessageBox submissionSubmitted = new MessageBox(shell);
                submissionSubmitted.setMessage("Submission accepted!");
                submissionSubmitted.open();
                break;
            }
            case TEST_FAILED : {
                MessageBox submissionTestFailed = new MessageBox(shell);
                if (result.getStatusText().length() > 150) {
                    submissionTestFailed.setMessage("Implementation is incorrect! Please look at the data in your selected filepath for more information.");
                    submissionTestFailed.open();
                    File file = new File("Test_Failed.log");
                    downloadHandler.writeInFile(file, result.getStatusText());
                    break;
                } else {
                    submissionTestFailed.setMessage(result.getStatusText());
                    submissionTestFailed.open();
                    break;
                }

            }

        }

    }

    public void setVariable(Button removeB) {
        blist.add(removeB);

    }

}
