import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Tracker extends Application {
	ArrayList<Issues> issue = new ArrayList<>();
	Hashtable<String, Users> users = new Hashtable<>();

	Scene logIn = null;
	Scene issueList = null;
	Scene detailedIssue = null;
	Scene addUser = null;
	Stage mainStage = null;
	GridPane pane1, pane2, pane3, pane4;
	TableView<Issues> table;

	TextField txtUsername, txtNewUser, txtNewPW, txtRePW, 
	txtTitle, txtDate, txtWhoTake;
	PasswordField txtPswd;

	TextArea txtIssueInfo, txtInfoText;

	ChoiceBox<String> choiceBox;
	ChoiceBox<String> devList;

	Button btnLogin, btnAddUser, btnLogOut, btnCancel, btnSubmitUser, btnCancel1,
	btnAssign, btnOpen, btnClose,btnReject, btnResolve, btnValidate, btnFail,
	btnSubmitIssue, btnNewIssue;

	String name, pswd, occupation;

	Label lbl1, lbl2, lbl3, lblIssueBar, lblTitleBar, lblDateBar, lblStatusBar,
	lblNameBar, lblWhoTake, lblIssueNo,lblReportor, lblStatus, fixedWhoTake,
	fixedDate, fixedTitle, lblOccupation, lblInfo;

	ComboBox<String>comboBx;
	int issueNo;

	public void start(Stage primaryStage) {

		users.put("Default", new Users("000", "Manager"));

		readUserFile();// load Users' name, password, occupation
		readissueListFile();// load issueList information
		mainStage = primaryStage;
		logIn = getLogIn();
		issueList = getIssueList();
		detailedIssue = getDetailedIssue();
		addUser = getAddUser();

		mainStage.setScene(logIn);
		mainStage.show();

	}

	public void switchScenes(Scene scene) {
		mainStage.setScene(scene);
	}

	// GET LOGIN INFO FROM USERS
	public Scene getLogIn() {
		pane1 = new GridPane();
		Label lblUsnm = new Label("User name: ");
		Label lblPswd = new Label("Password: ");
		lblUsnm.setPrefWidth(100);
		lblPswd.setPrefWidth(100);

		btnLogin = new Button("Log in");
		btnLogin.setPrefWidth(80);

		txtUsername = new TextField();// text field for user name
		txtUsername.setPromptText("User name");
		txtPswd = new PasswordField();// password field for users
		txtPswd.setPromptText("Password");
		pane1.setPadding(new Insets(70, 30, 10, 40));
		pane1.setVgap(8);
		pane1.setHgap(2);

		pane1.add(lblUsnm, 1, 0);
		pane1.add(lblPswd, 1, 1);
		pane1.add(txtUsername, 2, 0);
		pane1.add(txtPswd, 2, 1);
		pane1.add(btnLogin, 3, 3);

		btnLogin.setOnAction(e -> setInfo());

		return logIn = new Scene(pane1, 350, 250);
	}

//SET INFO FOR USERS IN THE LOG IN SCENE

	public void setInfo() {
		try {
			name = txtUsername.getText();// get user's name in the login text field
			pswd = txtPswd.getText();// get user's password from the log in scene
			occupation = users.get(name).getOccupation();// get user's occupation based on their name
// find the information from users' inputs
			logInAuthorize();
		} catch (Exception e) {
			showAlert("User name or password is not correct. Please try again! Setting Info issue");
		}
	}

//CONFIRM USERS' INPUT ON LOG IN SCENE AND CUSTOMIZE "ISSUE LIST" SCENE

	public void logInAuthorize() {
		// confirm user's name and password are correct
		try {
			if (pswd.equals(users.get(name).getPassword())) {
				// if correct, create label for the user's scene
				lbl1.setText(occupation + " : " + name);
				lbl2.setText(occupation + " : " + name);
				lbl3.setText("Manager: " + name);
				// if occupation is manager, manager can add New Users( add new user button)
				if (this.occupation.equals("Manager")) {
					pane2.add(btnAddUser, 4, 5);
					btnAddUser.setOnAction(e -> switchScenes(addUser));
				}
				switchScenes(issueList);
			} else {
				showAlert("User name or password is incorrect. Please try again! LOG IN ");
			}
		} catch (Exception e) {
			System.out.println("Authoring error: " + e);
			showAlert("The information is not valid!");
		}
	}

// ADD NEW USERS SCENE FOR (MANAGER)
	public Scene getAddUser() {

		try {
			pane4 = new GridPane();
			pane4.setHgap(5);
			pane4.setVgap(5);
			pane4.setPadding(new Insets(10, 0, 0, 10));

			lbl3 = new Label("");
			lbl3.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 20));
			txtNewUser = new TextField();
			txtNewPW = new TextField();
			txtRePW = new TextField();

			lblOccupation = new Label("");
			pane4.add(lblOccupation, 3, 8);

			choiceBox = new ChoiceBox<String>();
			choiceBox.getItems().addAll("User", "Manager", "Developer");
			choiceBox.getSelectionModel().select(0);

			pane4.add(choiceBox, 3, 7);

			VBox vb = new VBox();
			HBox hb = new HBox();
			hb.setPadding(new Insets(10, 10, 10, 10));
			hb.setSpacing(10);

			btnLogOut = new Button("Log out");
			btnCancel = new Button("Cancel");
			btnSubmitUser = new Button("Submit");
			hb.getChildren().addAll(btnSubmitUser, btnCancel);

			pane4.add(new Label("User Name: "), 0, 1);
			pane4.add(new Label("Password: "), 0, 3);
			pane4.add(new Label("Re-type Password: "), 0, 5);
			pane4.add(lbl3, 0, 0, 2, 1);
			pane4.add(txtNewUser, 1, 1, 2, 1);
			pane4.add(txtNewPW, 1, 3, 2, 1);
			pane4.add(txtRePW, 1, 5, 2, 1);
			pane4.add(new Label("Occcupation: "), 0, 7);
			pane4.add(vb, 1, 8);
			pane4.add(btnLogOut, 5, 0);
			pane4.add(hb, 0, 10);

			btnCancel.setOnAction(e -> switchScenes(issueList));
			btnSubmitUser.setOnAction(e -> addUser());

			btnLogOut.setOnAction(e -> {
				issueList = getIssueList();
				resetDetailedIssue();
				switchScenes(logIn);
			});

		} catch (Exception e) {
			System.out.println("Error getting AddUserScene : " + e);
		}
		return addUser = new Scene(pane4, 400, 300);
	}

	// TAKE "NEW USER'S INFORMATION FROM "ADDUSERSCENE" AND VALIDATE
	// if it is valid, store it into the HashTable and "userList.txt" file
	public void addUser() {
		String newOccupation = "";
		PrintWriter pw = null;
		FileWriter fw = null;

		// print writer and file writer for new developers
		PrintWriter newDev = null;
		FileWriter devFile = null;

		try {
			// make sure that the sentence formatted correctly so the file can be read
			boolean validate = true;

			// validate all the info make sure it does not contain all the special
			// characters and digits in the text field for user name
			for (int i = 0; i < txtNewUser.getText().length(); i++) {
				char c = txtNewUser.getText().charAt(i);
				if (c == '[' || c == ']' || c == '\\' || c == '.' || c == '"' || c == '(' || c == ')'
						|| Character.isDigit(c)) {
					validate = false;
					showAlert("Can not use special charaters and digit for user name area.");
					break;
				}
			}

			if (txtNewUser.getText().isEmpty() || txtNewPW.getText().isEmpty() || txtRePW.getText().isEmpty()) {
				// if any of these text field is blank, show the alert
				validate = false;
				showAlert("Please fill all the blanks. ");
			} else if (!txtNewPW.getText().equals(txtRePW.getText())) {
				// if 2 passwords is not matching, show the alert
				validate = false;
				showAlert("Please enter the same password.");
			} else {

				if (users.containsKey(txtNewUser.getText())) {
					validate = false;
					showAlert("This user name is already used, please use another one.");
				}
			}

			// WHen all the inputs are valid
			if (validate) {
				// when the input is valid, take the information from the drop down list
				if (this.choiceBox.getValue().equals("User")) {
					newOccupation = "User";

				} else if (this.choiceBox.getValue().equals("Developer")) {
					newOccupation = "Developer";
					
				} else {
					newOccupation = "Manager";
				}

				if (validate && this.choiceBox.getValue().equals("User")
						|| this.choiceBox.getValue().equals("Manager")) {
					// append the information into the "userList.txt"
					fw = new FileWriter("userList.txt", true);
					pw = new PrintWriter(fw);
					pw.println(txtNewUser.getText() + "[" + txtNewPW.getText() + "]" + newOccupation);
				} else if (this.choiceBox.getValue().equals("Developer")) {

					// append the information into the "userList.txt"
					fw = new FileWriter("userList.txt", true);
					pw = new PrintWriter(fw);
					pw.println(txtNewUser.getText() + "[" + txtNewPW.getText() + "]" + newOccupation);

					devFile = new FileWriter("devList.txt", true);
					newDev = new PrintWriter(devFile);
					newDev.println(txtNewUser.getText());

				}

				// put the information into the HashTable
				users.put("txtNewUser.getText()", new Users("txtNewPW.getText()", newOccupation));
				// set all the text field to blank after submit the info
				txtNewUser.setText("");
				txtNewPW.setText("");
				txtRePW.setText("");
				switchScenes(issueList);

			}
		} catch (Exception e) {
			System.out.println("Registering error: " + e);
		} finally {
			if (pw != null && newDev != null)
				pw.close();
			newDev.close();
		}
		readUserFile();

	}

	// MAKE issueList LIST INTO THE TABLE

	public Scene getIssueList() {
		try {

			pane2 = new GridPane();
			pane2.setPadding(new Insets(10, 10, 10, 10));
			pane2.setHgap(5);
			pane2.setVgap(5);

			lbl1 = new Label("");
			lbl1.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

			Label lblList = new Label("Issue List");
			lblList.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));

			table = new TableView<Issues>();
			table.setPrefHeight(330);
			table.setPrefWidth(550);
			// set the table for the issueList list
			TableColumn<Issues, Integer> issueNoColumn = new TableColumn<>("Issue No");
			// set value for each column
			issueNoColumn.setCellValueFactory(new PropertyValueFactory<>("issueNo"));

			TableColumn<Issues, Integer> titleColumn = new TableColumn<>("Title");
			titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

			TableColumn<Issues, Integer> dateColumn = new TableColumn<>("Date");
			dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

			TableColumn<Issues, Integer> statusColumn = new TableColumn<>("Status");
			statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

			table.setStyle("-fx-background-color:blue");
			table.getColumns().add(issueNoColumn);
			table.getColumns().add(titleColumn);
			table.getColumns().add(dateColumn);
			table.getColumns().add(statusColumn);

			// Add Issue info to the table from ArrayList except "closed" issueList
			for (int i = 0; i < issue.size(); i++) {
				if (!issue.get(i).getStatus().equals("Closed"))
					table.getItems().add(issue.get(i));
			}
			btnNewIssue = new Button("New Issue");
			btnNewIssue.setPrefWidth(82);

			btnLogOut = new Button("Log Out");
			btnLogOut.setPrefWidth(82);

			btnAddUser = new Button("Add person");
			btnAddUser.setPrefWidth(82);

			pane2.add(lblList, 0, 1);
			pane2.add(lbl1, 0, 0);
			pane2.add(table, 0, 2, 4, 1);
			pane2.add(btnNewIssue, 4, 4);
			pane2.add(btnLogOut, 4, 0);

			btnNewIssue.setOnAction(e -> {
				setNewIssue();
				switchScenes(detailedIssue);
				// same as the detail scene, but all the textFields are blank
				// add "submit" button to submit new issue to the issue list

			});

			// logout, all the textFields are blank and go back to the login scene
			btnLogOut.setOnAction(e -> {

				resetDetailedIssue();
				switchScenes(logIn);
			});

			// users can open issueList by double-click on issue
			table.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2) {
					// get the information from the issue that selected from the table
					String info = table.getSelectionModel().getSelectedItem() + "";
					// toString in the Issue class prints issueNumber
					// take the issue number of the selected issue
					int num = (int) Integer.parseInt(info) - 1;

					// parse the number to int and -1 because the index in the ArrayList is 0
					// is always one less than issue number
					setDetails(num);
					switchScenes(detailedIssue);
				}
			});
			issueList = new Scene(pane2, 470, 500);
		} catch (Exception e) {
			System.out.println("issueList list Error: " + e);
		}
		return issueList;
	}

	// Set details of issue in detailedIssue Scene
	public void setDetails(int num) {
		try {
			lblIssueNo.setText(String.valueOf(issue.get(num).getIssueNo()));
			fixedTitle.setText(issue.get(num).getTitle());
			fixedDate.setText(issue.get(num).getDate());
			lblReportor.setText(issue.get(num).getReportor());
			lblStatus.setText(issue.get(num).getStatus());
			txtInfoText.setText(issue.get(num).getInfo());

			pane3.add(fixedTitle, 2, 3);
			pane3.add(fixedDate, 2, 4);
			pane3.add(lblReportor, 2, 5);
			pane3.add(txtInfoText, 1, 9, 3, 11);

			// If the issue status is "New", a text field appears so that Manager
			// Manager assign task to a specific developer
			if (issue.get(num).getStatus().equals("New") && users.get(name).getOccupation().equals("Manager")) {
				pane3.add(comboBx, 2, 7);
			} else {
				pane3.add(fixedWhoTake, 2, 7);
				fixedWhoTake.setText(issue.get(num).getWhoTake());
			}

			// add button base on the cases

			// MAnager can assign new issue to developers
			if (issue.get(num).getStatus().equals("New") && occupation.equals("Manager")) {
				pane3.add(btnAssign, 3, 40);
				btnAssign.setOnAction(e -> changeStatus("Assigned", num, txtWhoTake.getText()));

				// issueList is assigned for developers, and they can either open or reject it
			} else if (issue.get(num).getStatus().equals("Assigned") && issue.get(num).getWhoTake().equals(name)
					&& occupation.equals("Developer")) {
				pane3.add(btnOpen, 3, 39);
				pane3.add(btnReject, 4, 39);
				btnOpen.setOnAction(e -> changeStatus("Opened", num, issue.get(num).getWhoTake()));
				btnReject.setOnAction(e -> changeStatus("Rejected", num, "N/A"));
//
			} else if ((issue.get(num).getStatus().equals("Opened") || issue.get(num).getStatus().equals("Failed"))
					&& issue.get(num).getWhoTake().equals(name) && occupation.equals("Developer")) {
				pane3.add(btnResolve, 3, 40);
				btnResolve.setOnAction(e -> changeStatus("Resolved", num, issue.get(num).getWhoTake()));
				
//manager can either fail or validate issues after they are resolved
			} else if (issue.get(num).getStatus().equals("Resolved") && occupation.equals("Manager")) {
				pane3.add(btnValidate, 3, 39);
				pane3.add(btnFail, 4, 39);
				btnFail.setOnAction(e -> changeStatus("Failed", num, issue.get(num).getWhoTake()));
				btnValidate.setOnAction(e -> changeStatus("Validated", num, issue.get(num).getWhoTake()));
//manager can either assign or close the issues
			} else if (issue.get(num).getStatus().equals("Rejected") && occupation.equals("Manager")) {
				pane3.add(btnAssign, 3, 39);
				pane3.add(btnClose, 4, 39);
				btnAssign.setOnAction(e -> changeStatus("Assigned", num, issue.get(num).getWhoTake()));
				btnClose.setOnAction(e -> changeStatus("Closed", num, issue.get(num).getWhoTake()));
// issues validated will be closed
			} else if (issue.get(num).getStatus().equals("Validated") && occupation.equals("Manager")) {
				pane3.add(btnClose, 3, 40);
				btnClose.setOnAction(e -> changeStatus("Closed", num, issue.get(num).getWhoTake()));
			}

		} catch (Exception e) {
			System.out.println("Error setting Detailed Issue list" + e);
		}
	}

	// CHANGE STATUS IN BOTH OBJECT AND "issueList.TXT"
	public void changeStatus(String newStatus, int num, String whoTake) {
		File issueOverWrite = null;
		PrintWriter pw2 = null;

		try {
			if (issue.get(num).getStatus().equals("New") && whoTake.equals("")) {
				// show Alert if Manager did not input developer's name when they assign new
				// issueList
				showAlert("Please enter the developer's name to assign new issue.");
			} else if (!(users.containsKey(whoTake) && users.get(whoTake)
					.getOccupation().equals("Developer"))
					|| whoTake.equals("N/A")) {
				
				// if the manager assign the issue to someone that is not developer
				showAlert("This person is not developer., IN the change status code");
			} else {
				// set "newStatus" in object
				issue.get(num).setStatus(newStatus);
				issue.get(num).setWhoTake(whoTake);

				issueOverWrite = new File("issueList.txt");
				pw2 = new PrintWriter(issueOverWrite);
				// overwrite "issueList.txt" with new Status

				for (int i = 0; i < issue.size(); i++) {
					pw2.println(issue.get(i).getIssueNo() + "[" + issue.get(i).getTitle() + "]" + issue.get(i).getDate()
							+ "[" + issue.get(i).getInfo() + "]" + issue.get(i).getReportor() + "["
							+ issue.get(i).getStatus() + "]" + issue.get(i).getWhoTake());
				}
				resetIssueList();
				resetDetailedIssue();
				switchScenes(issueList);
				pw2.close();

			}
		} catch (Exception e) {
			System.out.println("Error with writing the status." + e);
		}

	}

	// SET information for the addNewIssue scene
	public void setNewIssue() {
		try {
			pane3.add(txtTitle, 2, 3);
			pane3.add(txtDate, 2, 4);
			pane3.add(lblReportor, 2, 5);
			pane3.add(fixedWhoTake, 2, 7);
			pane3.add(txtInfoText, 1, 9, 3, 11);
			pane3.add(btnSubmitIssue, 3, 40);
		//	pane3.add(comboBx, 2, 7, 4,1);

			lblReportor.setText(name);
			// lblWhoTake.setText("Assigned to: ");

			/*
			 * Making a drop-down list that contain all the developers name so
			 *  manager can choose from the list
			 */

			txtWhoTake.setText("");
			txtWhoTake.setPromptText("N/A");

			// lblWhoTake.setText("");
			btnSubmitIssue.setOnAction(e -> addNewIssue());
		} catch (Exception e) {
			System.out.println("Error setting new issueList.");
		}
	}

	/*
	 * Making the drop-down list of all the developers' name so managers can choose
	 * from the list
	 */
	/*
	 * 1. Read the info from the usersList.txt . Get the name based on the
	 * occupation Make them go to the list on the assign new issue
	 */

	public void addNewIssue() {

		FileWriter fw = null;
		PrintWriter pw = null;

		try {
			boolean validationInfo = true;
			// check if any fields is empty
			if (txtTitle.getText().equals("") || txtDate.getText().equals("") 
					|| txtInfoText.getText().equals("")) {
				
				validationInfo = false;
				showAlert("Please fill all the blanks.");
			}
			

			if (validationInfo) {
				for (int i = 0; i < txtInfoText.getText().length(); i++) {
					char c = txtInfoText.getText().charAt(i);
					if (c == '[' || c == ']' || c == 10) {
						validationInfo = false;
						showAlert("Can not use [] or Enter key to insert new lines");
						break;
					}
				}
			}

			if (validationInfo) {
				
				// issueNo the number of last issue, counted in parseIssueInfo()
				fw = new FileWriter("issueList.txt", true);
				pw = new PrintWriter(fw);
				
				// Append the new issueList into the "issueList.txt" file in the format below
				pw.println(issueNo + "[" + txtTitle.getText() + "]" + 
				txtDate.getText() + "[" + txtInfoText.getText()
						+ "]" + name + "[" + "New" + "]" + txtWhoTake.getText());
				
				// create new issue Object
				Issues newIssue = new Issues(issueNo, txtTitle.getText(), 
						txtDate.getText(), txtInfoText.getText(),
						name, "New", "");
				
				// add object to the ArrayList
				issue.add(newIssue);
				// Add the newIssue to the issueList table
				table.getItems().add(newIssue);
				// increment issueNo every time adding new issue
				issueNo++;

				resetDetailedIssue();
				switchScenes(issueList);

				txtTitle.setText("");
				txtDate.setText("");
				txtInfoText.setText("");
			}

		} catch (Exception e) {
			System.out.println("Can not read the issue.txt: " + e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	// SCene for submitting new issueList and checking issue details
	public Scene getDetailedIssue() {
		try {
			pane3 = new GridPane();
			pane3.setPadding(new Insets(10, 10, 10, 10));
			pane3.setVgap(4);
			pane3.setHgap(5);

			lbl2 = new Label("");
			lbl2.setFont(Font.font("Verdana", FontWeight.BOLD, 
					FontPosture.REGULAR, 20));

			lblIssueBar = new Label("No.");
			lblTitleBar = new Label("Title: ");
			lblDateBar = new Label("Date: ");
			lblStatusBar = new Label("Status: ");
			lblNameBar = new Label("Reported by: ");
			lblWhoTake = new Label("Assigned to: ");
			lblWhoTake.setPrefWidth(100);

			lblInfo = new Label("Information: ");

			lblIssueNo = new Label("");
			lblIssueNo.setPrefWidth(150);

			txtTitle = new TextField();
			txtDate = new TextField();

			lblReportor = new Label();
			lblStatus = new Label("New");

			txtWhoTake = new TextField("");
			txtWhoTake.setPromptText("N/A");

			txtInfoText = new TextArea();
			txtInfoText.setPrefSize(350, 100);
			txtInfoText.setWrapText(true);
			txtInfoText.setPromptText("IssueList's details");

			// set title, date and person who take the assignment
			fixedWhoTake = new Label("");
			fixedDate = new Label();
			fixedTitle = new Label();

			btnLogOut = new Button("LogOut");
			btnLogOut.setPrefWidth(80);

			btnSubmitIssue = new Button("Submit");
			btnSubmitIssue.setPrefWidth(80);

			btnCancel1 = new Button("Cancel");
			btnCancel1.setPrefWidth(80);

			btnAssign = new Button("Assign");
			btnAssign.setPrefWidth(80);

			btnOpen = new Button("Open");
			btnOpen.setPrefWidth(80);

			btnClose = new Button("Close");
			btnClose.setPrefWidth(80);

			btnReject = new Button("Reject");
			btnReject.setPrefWidth(80);

			btnResolve = new Button("Resolve");
			btnResolve.setPrefWidth(80);

			btnValidate = new Button("Validate");
			btnValidate.setPrefWidth(82);

			btnFail = new Button("Fail");
			btnFail.setPrefWidth(80);

			pane3.add(lbl2, 0, 0, 4, 1);
			pane3.add(btnLogOut, 4, 0);
			pane3.add(lblIssueBar, 1, 2);
			pane3.add(lblTitleBar, 1, 3);
			pane3.add(lblDateBar, 1, 4);
			pane3.add(lblNameBar, 1, 5);
			pane3.add(lblStatusBar, 1, 6);
			pane3.add(lblWhoTake, 1, 7);
			pane3.add(lblIssueNo, 2, 2);
			pane3.add(lblStatus, 2, 6);
			pane3.add(lblInfo, 1, 8);
			

			pane3.add(btnCancel1, 4, 40);

			btnCancel1.setOnAction(e -> {
				lblIssueNo.setText("");
				txtTitle.setText("");
				txtDate.setText("");
				lblReportor.setText("");
				lblStatus.setText("");
				txtWhoTake.setText("");
				txtInfoText.setText("");
				
				// reset the detail issue and switch the scene back to the issue list
				resetDetailedIssue();
				switchScenes(issueList);
			});

			// logOut button reset the issue list and switch back to the logIn scene
			btnLogOut.setOnAction(e -> {
				issueList = getIssueList();
				resetDetailedIssue();
				switchScenes(logIn);
			});

			detailedIssue = new Scene(pane3, 375, 400);

		} catch (Exception e) {
			System.out.println("Error getting detailedIssueScene" + e);
		}

		return detailedIssue;
	}
	
	

	// change the setting of buttons depending on the "status", need to reset
	// detailed scene
	public void resetDetailedIssue() {
		detailedIssue = getDetailedIssue();
		lbl2.setText(occupation + ":" + name);
		lblIssueNo.setText("");
		txtTitle.setText("");
		txtDate.setText("");
		lblReportor.setText("");
		lblStatus.setText("New");
		txtInfoText.setText("");

	}
	
	

	// WHEN ISSULIST SCENE IS UPDATED, THE "OCCUPATION: USERNAME" NEED TO BE ADDED
	// IF THE USER IS MANAGER
	public void resetIssueList() {
		issueList = getIssueList();
		lbl1.setText(occupation + ":" + name);

		// if occupation is Manager, adding the "add new user function button"
		if (this.occupation.equals("Manager")) {
			pane2.add(btnAddUser, 4, 5);
			btnAddUser.setOnAction(e -> switchScenes(addUser));
		}
	}

	// READ THE USER'S INFOMATION FROM THE FILE INTO THE SCENE
	public void readUserFile() {
		Scanner in = null;
		File userList = null;
		
		try {
			// put all the information into the file called userList.txt
			userList = new File("userList.txt");
			// scan all the information in the file into the scene
			in = new Scanner(userList);

			/*
			 * read the file line by line till the end of the file and send the info the
			 * parseUserInfo() to store the info into the HashTable
			 */
			while (in.hasNext()) {
				String userString = in.nextLine();
				parseUserList(userString);

			}

		} catch (Exception e) {
			System.out.println("Can't read userList.txt " + e);
		} finally {
			if (in != null)
				in.close();
		}

	}

	/*
	 * String format store in the "userList.txt: UserName[Password]Occupation Split
	 * them where "[" "]" and store them to the HashTable
	 */

	public void parseUserList(String userString) {
		// determine where "[" or "]" is from the toString() in the Users class
		// Example: Jenny[000]Developer

		int openSquareBracket = userString.indexOf("[");
		int closeSquareBracket = userString.indexOf("]");
		// cut the formatted String where "[" or "]" is
		String names = userString.substring(0, openSquareBracket)
				.trim();/*
						 * trim the String from index 0 to the open square
						 *  bracket to get user's name
						 */

		String passwords = userString.substring(openSquareBracket + 1, 
				closeSquareBracket).trim();/*
						 * trim the String from the index after the
						 * open square bracket to the index of closeSquareBracket
						 */

		String occupations = userString.substring(closeSquareBracket + 1).trim();
		// store the info to the HashTable
		users.put(names, new Users(passwords, occupations));

	}

	
	// Read the issueList file from the file called issueList.txt into the scene
	public void readissueListFile() {
		Scanner in = null;
		try {
			// create a file called issueList.txt
			File issueList = new File("issueList.txt");
			// scan all the info from the file to the scene
			in = new Scanner(issueList);

			/*
			 * read the file line by line till the end and send parseissueListInfo() to
			 * store info into the ArrayList
			 */
			while (in.hasNext()) {
				String info = in.nextLine();
				parseIssueInfo(info);

			}
			in.close();
			issueNo++;

		} catch (Exception e) {
			System.out.println("Can not read the issueList.txt" + e);
		}
	}

	// issueListString format: 1[title]date[info]reportor[status]
	// Split them where "[" or "]" is and store them to the HashTable

	public void parseIssueInfo(String issueListString) {
		// determine where 3 set of square brackets "[" or "]" are.
		int openSquareBrac1 = issueListString.indexOf("[");
		int closeSquareBrac1 = issueListString.indexOf("]");

		int openSquareBrac2 = issueListString.indexOf("[", openSquareBrac1 + 1);
		int closeSquareBrac2 = issueListString.indexOf("]", closeSquareBrac1 + 1);

		int openSquareBrac3 = issueListString.indexOf("[", openSquareBrac2 + 1);
		int closeSquareBrac3 = issueListString.indexOf("]", closeSquareBrac2 + 1);

		// cut the formatted string where the "[" and "]" are to get the information
		issueNo = Integer.parseInt(issueListString.substring(0,
				openSquareBrac1));/* get the issue number from the index 0 to open square bracket 1 */
		String title = issueListString.substring(openSquareBrac1 + 1, closeSquareBrac1).trim();
		String date = issueListString.substring(closeSquareBrac1 + 1, openSquareBrac2).trim();
		String info = issueListString.substring(openSquareBrac2 + 1, closeSquareBrac2).trim();
		String reportor = issueListString.substring(closeSquareBrac2 + 1, openSquareBrac3).trim();
		String savedStatus = issueListString.substring(openSquareBrac3 + 1, closeSquareBrac3).trim();
		String whoTake = issueListString.substring(closeSquareBrac3 + 1).trim();
		// add the info to the ArrayList as for the new object
		issue.add(new Issues(issueNo, title, date, info, reportor, savedStatus, whoTake));

	}

	// SHOW ALERT TO USERS
	public void showAlert(String msg) {
		Alert a = new Alert(Alert.AlertType.WARNING);
		a.setTitle("");
		a.setHeaderText("");
		a.setContentText(msg);
		a.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
