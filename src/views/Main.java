package views;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import engine.Game;
import engine.Player;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;

public class Main extends Application {
	Stage Main;
	Scene Start;
	Scene GameIntilization;
	Scene BoardScene;
	Scene ChampionSelection;
	Scene LeaderChoosing;
	Game game;
	Player player1;
	Player player2;
	VBox info = new VBox();
	VBox Actions = new VBox();
	GridPane central;
	Ability a;
	Label l1 = new Label();
	Label l2 = new Label();

	boolean choosingflag = true;
	int counter = 0;
	boolean leader1chosen;
	boolean leader2chosen;
	boolean moveClicked = false;
	boolean attackClicked = false;
	boolean AbilityClicked = false;
	boolean AI;
	Random rand = new Random();

	@Override
	public void start(Stage arg0) throws Exception, IOException {
		Main = new Stage();
		VBox p = new VBox();
		HBox h1 = new HBox();
		HBox h2 = new HBox();
		HBox h3 = new HBox();
		Start = new Scene(p, 1550, 860);
		// Pane p1 = new Pane();
		// BoardScene = BoardSceneMaker();
		GameIntilization = GameSceneMaker();

		Button start = new Button("Start Game");
		Button start2 = new Button("Start Game VS AI");
		start.setPrefSize(150, 100);
		start2.setPrefSize(150, 100);
		start.setLayoutX(400);
		start.setLayoutY(400);
		p.getChildren().addAll(h1,h2,h3);
		h2.getChildren().addAll(start,start2);
		h2.setSpacing(200);
		// p.getChildren().add(new ImageView(new Image("endgame.jpg")));
		start.setOnAction(e -> ChangeScene(GameIntilization));
		start2.setOnAction(e -> {
			ChangeScene(GameWithAISceneMaker());
			AI = true;
		});
		// checkGameOveer();
		// singleTarget();
		Main.setScene(Start);
		Main.show();

	}

	public void ChangeScene(Scene s) {
		Main.setScene(s);
	}

	public Scene GameWithAISceneMaker() {
		HBox p = new HBox();
		Scene root = new Scene(p, 1550, 860);
		TextField name1 = new TextField();
		Button btn = new Button("Finish");
		btn.setOnAction(e -> {
			player1 = new Player(name1.getText());
			player2 = new Player("Computer");
			try {
				ChampionSelection = ChampionSelectionSceneMake();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ChangeScene(ChampionSelection);
		});

		p.getChildren().addAll(name1, btn);
		System.out.println(name1.getText());
		p.setPrefSize(500, 1550);
		p.setAlignment(Pos.CENTER);
		name1.setAlignment(Pos.CENTER_LEFT);
		p.setSpacing(70);
		// game = new Game(player1,player2);
		BackgroundImage bg = new BackgroundImage(new Image("a33.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bGround = new Background(bg);
		p.setBackground(bGround);
		return root;

	}

	public Scene GameSceneMaker() {
		HBox p = new HBox();
		Scene root = new Scene(p, 1550, 860);
		TextField name1 = new TextField();
		TextField name2 = new TextField();
		Button btn = new Button("Finish");
		btn.setOnAction(e -> {
			player1 = new Player(name1.getText());
			player2 = new Player(name2.getText());
			try {
				ChampionSelection = ChampionSelectionSceneMake();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ChangeScene(ChampionSelection);
		});

		p.getChildren().addAll(name1, name2, btn);
		System.out.println(name1.getText());
		p.setPrefSize(500, 1550);
		p.setAlignment(Pos.CENTER);
		name1.setAlignment(Pos.CENTER_LEFT);
		p.setSpacing(70);
		name2.setAlignment(Pos.CENTER_RIGHT);
		// game = new Game(player1,player2);
		BackgroundImage bg = new BackgroundImage(new Image("a33.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bGround = new Background(bg);
		p.setBackground(bGround);
		return root;

	}

	public Scene ChampionSelectionSceneMake() throws IOException {
		int j = 0;
		int c = 5;
		int x = 0;
		ArrayList<Button> buttons = new ArrayList<>();
		Pane p = new Pane();
		GridPane ChampionsGrid = new GridPane();
		p.getChildren().add(ChampionsGrid);
		String s = player1.getName() + " IS CHOOSING";
		l1.setText(s);
		l2.setText(player2.getName() + " IS CHOOSING");
		l1.setFont(Font.font(20));
		l1.setVisible(true);
		l2.setVisible(false);
		ChampionsGrid.add(l1, 2, 5, 2, 2);
		ChampionsGrid.add(l2, 2, 5, 2, 2);

		Scene root = new Scene(p, 1550, 860);
		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");

		BackgroundImage bg = new BackgroundImage(new Image("BG.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bGround = new Background(bg);
		p.setBackground(bGround);

		HBox p1 = new HBox();
		HBox p2 = new HBox();
		TextArea txt3 = new TextArea();
		txt3.setText(player2.getName() + "'s Team: ");
		txt3.setPrefSize(100, 20);
		p2.getChildren().add(txt3);
		p2.setLayoutX(750);
		p2.setLayoutY(500);
		TextArea t1 = new TextArea(player1.getName() + "'s Team: ");
		p1.getChildren().add(t1);
		t1.setPrefSize(100, 20);
		t1.setEditable(false);
		t1.setLayoutX(200);
		p1.setLayoutY(500);
		p.getChildren().addAll(p1, p2);
		p1.setAlignment(Pos.BOTTOM_LEFT);

		for (int i = 0; i < (Game.getAvailableChampions().size() / 5); i++) {
			while (j < c) {
				Button b = new Button();
				buttons.add(b);
				b.setText(Game.getAvailableChampions().get(j).getName());
				b.setWrapText(true);
				b.setTextAlignment(TextAlignment.CENTER);
				b.setPrefSize(100, 100);
				b.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent arg0) {
						try {

							Button choose = new Button("Choose Champion");

							int k = buttons.indexOf(b);
							Champion c = Game.getAvailableChampions().get(k);

							choose.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent arg0) {
									if (choosingflag) {
										player1.getTeam().add(c);
										ImageView iv = getImage(c);
										p1.getChildren().add(iv);
										b.setStyle("-fx-background-color: red;");
										b.setDisable(true);
										l2.setText(player2.getName() + " IS CHOOSING");
										l2.setFont(Font.font(20));
										l2.setVisible(true);
										l1.setVisible(false);
										System.out.println(player1.getTeam().toString() + "\n" + "\n"
												+ player2.getTeam().toString());
										if (AI) {
											int x = rand.nextInt(15);
											Button b = buttons.get(x);
											Champion comp = Game.getAvailableChampions().get(x);
											while (player1.getTeam().contains(comp)
													|| player2.getTeam().contains(comp)) {
												x = rand.nextInt(15);
												b = buttons.get(x);
												comp = Game.getAvailableChampions().get(x);
											}
											player2.getTeam().add(comp);
											ImageView iv2 = getImage(comp);
											p2.getChildren().add(iv2);
											b.setStyle("-fx-background-color: blue;");
											b.setDisable(true);
											l1.setVisible(true);
											l2.setVisible(false);
											counter++;
										} else
											choosingflag = false;

									} else {
										player2.getTeam().add(c);
										choosingflag = true;
										ImageView iv = getImage(c);
										p2.getChildren().add(iv);
										b.setStyle("-fx-background-color: blue;");
										b.setDisable(true);
										String s = player1.getName() + " IS CHOOSING";
										l1.setText(s);
										l1.setFont(Font.font(20));
										l1.setVisible(true);
										l2.setVisible(false);

									}
									counter++;
									if (counter == 6) {
										game = new Game(player1, player2);
										Main.setScene(leaderChoosingScene());

									}
									// b.setVisible(false);
									choose.setVisible(false);
								}

							});
							ChampionsGrid.add(choose, 7, 4);
							TextArea textArea = new TextArea();
							textArea.setEditable(false);
							Button b = (Button) arg0.getSource();
							textArea.appendText(Game.getAvailableChampions().get(k).toString());
							ChampionsGrid.add(textArea, 6, 0, 3, 2);
							textArea.setWrapText(true);

						} catch (Exception e) {
							System.out.println(e.getMessage());
						}

					}

				});
				ChampionsGrid.add(b, x, i);
				j++;
				x++;
			}
			c = c + 5;
			x = 0;
		}
		// ChampionsGrid.setGridLinesVisible(true);
		return root;
	}

	public Scene leaderChoosingScene() {
		// Pane p = new Pane();
		HBox main = new HBox();
		VBox all = new VBox();
		BackgroundImage bg = new BackgroundImage(new Image("BG.png"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bGround = new Background(bg);
		all.setBackground(bGround);
		VBox v1 = new VBox();
		VBox v2 = new VBox();

		Text t = new Text();
		t.setText("Players Choose your leaders");
		t.setFont(Font.font(50));
		all.setSpacing(120);
		main.getChildren().addAll(v1, v2);
		all.getChildren().addAll(t, main);
		main.setAlignment(Pos.CENTER);
		all.setAlignment(Pos.CENTER);
		main.setSpacing(200);
		for (Champion c : player1.getTeam()) {
			ImageView pic = getImage(c);
			v1.getChildren().add(pic);
			pic.setPickOnBounds(true);
			pic.setOnMouseClicked(e -> {
				player1.setLeader(c);

				v1.getChildren().clear();
				if (AI)
					Main.setScene(BoardSceneMaker());
			});

		}
		if (AI) {
			int x = rand.nextInt(3);
			player2.setLeader(player2.getTeam().get(x));
		} else {
			for (Champion c : player2.getTeam()) {
				ImageView pic = getImage(c);
				pic.setPickOnBounds(true);
				v2.getChildren().add(pic);
				pic.setOnMouseClicked(e -> {
					player2.setLeader(c);
					System.out.println(player2.getLeader().getName());
					Main.setScene(BoardSceneMaker());
				});

			}
		}
		main.setAlignment(Pos.CENTER);
		LeaderChoosing = new Scene(all, 1550, 860);
		return LeaderChoosing;

	}

	public Scene BoardSceneMaker() {
		SplitPane main = new SplitPane();
		central = new GridPane();
		info = rightBoxMaker();
		leftBoxMaker();

		info.setPrefSize(300, 1550);
		Scene root = new Scene(main, 1550, 860);
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				Tile tile = new Tile((x + y) % 2 == 0, x, y);
				ColumnConstraints col = new ColumnConstraints();
				col.setHalignment(HPos.CENTER);
				central.getColumnConstraints().add(col);
				RowConstraints row = new RowConstraints();
				row.setValignment(VPos.CENTER);
				central.getRowConstraints().add(row);
				central.add(tile, x, y);

			}
		}
		updateBoard();
		main.setDividerPositions(0.333, 0.667);
		// central.setMinWidth(850);
		main.getItems().addAll(Actions, central, info);
		return root;
	}

	public void Updateright(VBox v) {
		TextArea txt = (TextArea) v.getChildren().get(0);
		txt.clear();
		txt.appendText("Current Champion : \n \n");
		Champion c = game.getCurrentChampion();
		txt.setText(c.toString() + "\n");
		txt.appendText("---------------------------------------\n");

		if (game.isFirstLeaderAbilityUsed())
			txt.appendText(player1.getName() + "'s Team : " + "\n" + "Leader Ability Used : Used" + "\n" + "\n");
		else
			txt.appendText(player1.getName() + "'s Team : " + "\n" + "Leader Ability Used : Did Not Use" + "\n" + "\n");

		for (Champion c1 : player1.getTeam()) {
			if (c1.getCondition() != Condition.KNOCKEDOUT)
				txt.appendText(c1.toString1() + "\n" + "Leader: " + player1.getLeader().equals(c1) + "\n" + "\n");
		}
		txt.appendText("---------------------------------------\n");

		if (game.isSecondLeaderAbilityUsed())
			txt.appendText(player2.getName() + "'s Team : " + "\n" + "Leader Ability Used : Used" + "\n" + "\n");
		else
			txt.appendText(player2.getName() + "'s Team : " + "\n" + "Leader Ability Used : Did Not Use" + "\n" + "\n");

		for (Champion c1 : player2.getTeam()) {
			if (c1.getCondition() != Condition.KNOCKEDOUT)
				txt.appendText(c1.toString1() + "\n" + "Leader: " + player2.getLeader().equals(c1) + "\n" + "\n");
		}
	}

	public VBox rightBoxMaker() {
		VBox v = new VBox();
		Champion c = game.getCurrentChampion();

		TextArea txt = new TextArea();
		txt.setPrefHeight(1000);
		txt.appendText("Current Champion : \n \n");
		txt.setText(c.toString() + "\n");
		txt.appendText("---------------------------------------\n");
		if (game.isFirstLeaderAbilityUsed())
			txt.appendText(player1.getName() + "'s Team : " + "\n" + "Leader Ability Used : Used" + "\n" + "\n");
		else
			txt.appendText(player1.getName() + "'s Team : " + "\n" + "Leader Ability Used : Did Not Use" + "\n" + "\n");

		for (Champion c1 : player1.getTeam()) {
			if (c1.getCondition() != Condition.KNOCKEDOUT)
				txt.appendText(c1.toString1() + "\n" + "Leader: " + player1.getLeader().equals(c1) + "\n" + "\n");
		}
		txt.appendText("---------------------------------------\n");
		if (game.isSecondLeaderAbilityUsed())
			txt.appendText(player2.getName() + "'s Team : " + "\n" + "Leader Ability Used : Used" + "\n" + "\n");
		else
			txt.appendText(player2.getName() + "'s Team : " + "\n" + "Leader Ability Used : Did Not Use" + "\n" + "\n");

		for (Champion c1 : player2.getTeam()) {
			if (c1.getCondition() != Condition.KNOCKEDOUT)
				txt.appendText(c1.toString1() + "\n" + "Leader: " + player2.getLeader().equals(c1) + "\n" + "\n");
		}
		txt.setLayoutX(200);
		txt.setLayoutY(0);
		txt.setWrapText(true);
		v.getChildren().addAll(txt);
		return v;
	}

	public void errorWindow(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(message);
		alert.setWidth(500);
		alert.setHeight(100);
		alert.showAndWait();
	}

	public void computerPlay() {
		Champion c = game.getCurrentChampion();
		boolean flag = true;
		while (player2.getTeam().contains(game.getCurrentChampion()) && c.getCurrentActionPoints() >= 1) { // for(int
																											// i=0;i<1
																											// &&
																											// flag;i++)
			int x = rand.nextInt(6);
			switch (x) {
			case 0:
				try {
					game.move(Direction.DOWN);
					updateBoard();
				} catch (NotEnoughResourcesException | UnallowedMovementException e1) {

					try {
						game.move(Direction.RIGHT);
						updateBoard();
					} catch (NotEnoughResourcesException | UnallowedMovementException e) {
						try {
							game.move(Direction.LEFT);
							updateBoard();
						} catch (NotEnoughResourcesException | UnallowedMovementException e2) {
							try {
								game.move(Direction.UP);
								updateBoard();
							} catch (NotEnoughResourcesException | UnallowedMovementException e3) {
								break;
							}

						}

					}

				}

				break;
			case 1:
				try {
					game.attack(Direction.DOWN);
					updateBoard();

				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
					try {
						game.attack(Direction.RIGHT);
						updateBoard();

					} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
						try {
							game.attack(Direction.LEFT);
							updateBoard();

						} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e2) {
							try {
								game.attack(Direction.UP);
								updateBoard();

							} catch (NotEnoughResourcesException | ChampionDisarmedException
									| InvalidTargetException e3) {
							}
						}

					}

				}
				if (isOver())
					return;

				break;
			case 2:
				AIcastAbility(c);
				updateBoard();
				if (isOver())
					return;
				break;
			case 3:
				if (player2.getLeader().equals(c)) {
					try {
						game.useLeaderAbility();
						updateBoard();
					} catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException e) {
					}
				}
				if (isOver())
					return;
				break;
			case 4:
				game.endTurn();
				if (!player2.getTeam().contains(game.getCurrentChampion())) {
					leftBoxMaker();
					return;

				}
				break;
			}

			c = game.getCurrentChampion();
		}

		if (!(c.getCurrentActionPoints() >= 1))
			game.endTurn();

		// game.endTurn();
		leftBoxMaker();
	}

	public void AIcastAbility(Champion c) {
		Ability a = c.getAbilities().get(rand.nextInt(3));
		if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET
				|| a.getCastArea() == AreaOfEffect.SURROUND) {
			try {
				game.castAbility(a);

			} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e) {

			}
		}

		if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
			int x = rand.nextInt(5);
			int y = rand.nextInt(5);
			int distance = Math.abs(c.getLocation().x - x) + Math.abs(c.getLocation().y - y);
			while (distance > a.getCastRange() || game.getBoard()[x][y] == null) {
				x = rand.nextInt(5);
				y = rand.nextInt(5);
				distance = Math.abs(c.getLocation().x - x) + Math.abs(c.getLocation().y - y);
			}
			try {
				game.castAbility(a, x, y);

			} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException
					| CloneNotSupportedException e) {

			}
		}

		if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
			try {
				game.castAbility(a, Direction.DOWN);

			} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e) {
				try {
					game.castAbility(a, Direction.RIGHT);

				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					try {
						game.castAbility(a, Direction.LEFT);

					} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e2) {
						try {
							game.castAbility(a, Direction.UP);

						} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e3) {
						}
					}
				}
			}
		}
	}

	public boolean isOver() {
		return game.checkGameOver() != null;
	}

	public void leftBoxMaker() {
		Actions.getChildren().clear();
		Button up = new Button("UP");
		Button right = new Button("RIGHT");
		Button left = new Button("LEFT");
		Button down = new Button("DOWN");
		Button endTurn = new Button("End Turn");
		Button move = new Button("MOVE");
		Button attack = new Button("Attack");
		Button leaderAbility = new Button("Use Leader Ability");
		VBox normalActions = new VBox();
		GridPane abilities = new GridPane();
		VBox turnOrder = new VBox();
		TextArea txt = new TextArea();
		ArrayList<Button> buttons = new ArrayList<>();
		// boolean flag = true;
		normalActions.getChildren().addAll(up, right, left, down, move, attack, endTurn, leaderAbility);
		// Actions.setSpacing(200);
		abilities.setPrefSize(400, 500);

		Champion c = game.getCurrentChampion();

		if (AI && player2.getTeam().contains(c)) {
			Actions.getChildren().clear();
			computerPlay();
			Updateright(info);
			checkGameOveer();
			// leftBoxMaker();
			// rightBoxMaker();
		} else {
			abilities.getChildren().clear();
			for (int i = 0; i < c.getAbilities().size(); i++) {
				Button b = new Button(c.getAbilities().get(i).getName());
				b.setWrapText(true);
				buttons.add(b);
				b.setPrefSize(185, 230);
				abilities.add(b, 0, i);
				TextArea txt1 = new TextArea();
				txt1.setEditable(false);
				txt1.setWrapText(true);
				txt1.setPrefSize(300, 230);
				txt1.setText(c.getAbilities().get(i).displayAbility());
				abilities.add(txt1, 1, i);
				b.setLayoutY(60 * (i + 1));
				b.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						Button b = (Button) arg0.getSource();
						int k = buttons.indexOf(b);
						a = c.getAbilities().get(k);

						if (a.getCastArea() == AreaOfEffect.SURROUND || a.getCastArea() == AreaOfEffect.SELFTARGET
								|| a.getCastArea() == AreaOfEffect.TEAMTARGET) {
							try {
								game.castAbility(a);
								txt1.setText(c.getAbilities().get(k).displayAbility());
								Updateright(info);
								updateBoard();
								checkGameOveer();
							} catch (NotEnoughResourcesException e) {
								errorWindow(e.getMessage());
							} catch (AbilityUseException e) {
								errorWindow(e.getMessage());
							} catch (CloneNotSupportedException e) {
								errorWindow(e.getMessage());
							}
						}

						if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
							// b.setStyle("-fx-background-color: lightgreen;");
							AbilityClicked = true;
						}

						if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
							singleTarget();

						}

					}

				});

			}

			txt.setText(game.getTurnOrder().toString());
			txt.setWrapText(true);
			txt.setEditable(false);

			turnOrder.setAlignment(Pos.BOTTOM_CENTER);

			move.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (moveClicked) {
						move.setStyle("");
						moveClicked = false;

					} else {
						if (!attackClicked) {
							move.setStyle("-fx-background-color: lightgreen;");
							attackClicked = false;
							moveClicked = true;
						} else {
							move.setStyle("-fx-background-color: lightgreen;");
							attack.setStyle("");
							moveClicked = true;
							attackClicked = false;
						}

					}

				}

			});
			attack.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (attackClicked) {
						attack.setStyle("");
						attackClicked = false;
					} else {
						if (!moveClicked) {
							attack.setStyle("-fx-background-color: lightgreen;");
							moveClicked = false;
							attackClicked = true;
						} else {
							attack.setStyle("-fx-background-color: lightgreen;");
							move.setStyle("");
							attackClicked = true;
							moveClicked = false;
						}

					}

				}

			});
			leaderAbility.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					try {
						game.useLeaderAbility();
						Updateright(info);
						updateBoard();
					} catch (LeaderNotCurrentException e) {
						errorWindow(e.getMessage());

					} catch (LeaderAbilityAlreadyUsedException e) {
						errorWindow(e.getMessage());
					}

				}

			});

			endTurn.setOnAction(e -> {
				game.endTurn();
				Updateright(info);
				leftBoxMaker();
			});
			Actions.getChildren().addAll(normalActions, abilities, txt);

			up.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (moveClicked) {
						try {
							game.move(Direction.DOWN);
							moveClicked = false;
							move.setStyle("");
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());

						} catch (UnallowedMovementException e) {
							errorWindow(e.getMessage());
						}
					}

					if (attackClicked) {
						try {
							game.attack(Direction.DOWN);
							checkGameOveer();
							attackClicked = false;
							attack.setStyle("");
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (ChampionDisarmedException e) {
							errorWindow(e.getMessage());
						} catch (InvalidTargetException e) {
							errorWindow(e.getMessage());
						}
					}

					if (AbilityClicked) {
						try {
							game.castAbility(a, Direction.DOWN);
							checkGameOveer();
							leftBoxMaker();
							AbilityClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (AbilityUseException e) {
							errorWindow(e.getMessage());
						} catch (CloneNotSupportedException e) {
							errorWindow(e.getMessage());
						}
					}

				}

			});

			left.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (moveClicked) {
						try {
							game.move(Direction.LEFT);
							moveClicked = false;
							move.setStyle("");
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());

						} catch (UnallowedMovementException e) {
							errorWindow(e.getMessage());
						}
					}
					if (attackClicked) {
						try {
							game.attack(Direction.LEFT);
							checkGameOveer();
							attack.setStyle("");
							attackClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (ChampionDisarmedException e) {
							errorWindow(e.getMessage());
						} catch (InvalidTargetException e) {
							errorWindow(e.getMessage());
						}
					}

					if (AbilityClicked) {
						try {
							game.castAbility(a, Direction.LEFT);
							checkGameOveer();
							leftBoxMaker();
							AbilityClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (AbilityUseException e) {
							errorWindow(e.getMessage());
						} catch (CloneNotSupportedException e) {
							errorWindow(e.getMessage());
						}
					}

				}

			});

			right.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (moveClicked) {
						try {
							game.move(Direction.RIGHT);
							moveClicked = false;
							move.setStyle("");
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (UnallowedMovementException e) {
							errorWindow(e.getMessage());
						}
					}
					if (attackClicked) {
						try {
							game.attack(Direction.RIGHT);
							checkGameOveer();
							attack.setStyle("");
							attackClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (ChampionDisarmedException e) {
							errorWindow(e.getMessage());
						} catch (InvalidTargetException e) {
							errorWindow(e.getMessage());
						}
					}

					if (AbilityClicked) {
						try {
							game.castAbility(a, Direction.RIGHT);
							checkGameOveer();
							leftBoxMaker();
							AbilityClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (AbilityUseException e) {
							errorWindow(e.getMessage());
						} catch (CloneNotSupportedException e) {
							errorWindow(e.getMessage());
						}
					}

				}

			});

			down.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (moveClicked) {
						try {
							game.move(Direction.UP);
							moveClicked = false;
							move.setStyle("");
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());

						} catch (UnallowedMovementException e) {
							errorWindow(e.getMessage());
						}
					}
					if (attackClicked) {
						try {
							game.attack(Direction.UP);
							checkGameOveer();
							attack.setStyle("");
							attackClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (ChampionDisarmedException e) {
							errorWindow(e.getMessage());
						} catch (InvalidTargetException e) {
							errorWindow(e.getMessage());
						}
					}
					if (AbilityClicked) {
						try {
							game.castAbility(a, Direction.UP);
							checkGameOveer();
							leftBoxMaker();
							AbilityClicked = false;
							Updateright(info);
							updateBoard();
						} catch (NotEnoughResourcesException e) {
							errorWindow(e.getMessage());
						} catch (AbilityUseException e) {
							errorWindow(e.getMessage());
						} catch (CloneNotSupportedException e) {
							errorWindow(e.getMessage());
						}
					}

				}

			});
		}

		// txt.setLayoutY(850);

	}
	/*
	 * public void killYoussef() { boolean flag=true; if(flag) { //youssef.pewpew();
	 * } }
	 */

	public void updateBoard() {
		central.getChildren().clear();
		Image img = null;
		ImageView imgV = null;
		Tooltip h = null;
		for (int i = 0; i < Game.getBoardwidth(); i++) {
			for (int j = 0; j < Game.getBoardheight(); j++) {
				Damageable d = (Damageable) game.getBoard()[i][j];

				if (d == null) {
					Tile tile = new Tile((i + j) % 2 == 0, j, i);
					central.add(tile, j, i);
				} else {
					if (d instanceof Cover) {
						Tile tile = new Tile((i + j) % 2 == 0, j, i);
						central.add(tile, j, i);
						h = new Tooltip(d.getCurrentHP() + "");
						imgV = new ImageView(new Image("Cover.png"));
						central.add(imgV, j, i);
						Tooltip.install(imgV, h);
					}

					if (d instanceof Champion) {
						Tile tile = new Tile((i + j) % 2 == 0, j, i);
						central.add(tile, j, i);
						switch (((Champion) d).getName()) {
						case "Captain America":
							ImageView view1 = new ImageView(new Image("Captain America.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view1, h);
							central.add(view1, j, i);
							break;
						case "Deadpool":
							ImageView view2 = new ImageView(new Image("DeadPool.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view2, h);
							central.add(view2, j, i);
							break;
						case "Dr Strange":
							ImageView view3 = new ImageView(new Image("Dr strange.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view3, h);
							central.add(view3, j, i);
							break;
						case "Quicksilver":
							ImageView view4 = new ImageView(new Image("Quick Silver.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view4, h);
							central.add(view4, j, i);
							break;
						case "Yellow Jacket":
							ImageView view5 = new ImageView(new Image("Yellow Jacket.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view5, h);
							central.add(view5, j, i);
							break;
						case "Ironman":
							ImageView view6 = new ImageView(new Image("Iron Man.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view6, h);
							central.add(view6, j, i);
							break;
						case "Venom":
							ImageView view7 = new ImageView(new Image("Venom.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view7, h);
							central.add(view7, j, i);
							break;
						case "Loki":
							ImageView view8 = new ImageView(new Image("Loki.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view8, h);
							central.add(view8, j, i);
							break;
						case "Hela":
							ImageView view9 = new ImageView(new Image("Hela.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view9, h);
							central.add(view9, j, i);
							break;
						case "Hulk":
							ImageView view10 = new ImageView(new Image("Hulk.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view10, h);
							central.add(view10, j, i);
							break;
						case "Ghost Rider":
							ImageView view11 = new ImageView(new Image("Ghost Rider.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view11, h);
							central.add(view11, j, i);
							break;
						case "Iceman":
							ImageView view12 = new ImageView(new Image("Ice Man.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view12, h);
							central.add(view12, j, i);
							break;
						case "Spiderman":
							ImageView view13 = new ImageView(new Image("Spider Man.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view13, h);
							central.add(view13, j, i);
							break;
						case "Thor":
							ImageView view14 = new ImageView(new Image("Thor.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view14, h);
							central.add(view14, j, i);
							break;
						case "Electro":
							ImageView view15 = new ImageView(new Image("electro.png"));
							h = new Tooltip(d.getCurrentHP() + "");
							Tooltip.install(view15, h);
							central.add(view15, j, i);
							break;

						}

					}
				}
			}
		}
	}

	public ImageView getImage(Champion c) {
		ImageView view = null;
		switch (((Champion) c).getName()) {
		case "Captain America":
			view = new ImageView(new Image("Captain America.png"));
			break;
		case "Deadpool":
			view = new ImageView(new Image("DeadPool.png"));
			break;
		case "Dr Strange":
			view = new ImageView(new Image("Dr strange.png"));
			break;
		case "Quicksilver":
			view = new ImageView(new Image("Quick Silver.png"));
			break;
		case "Yellow Jacket":
			view = new ImageView(new Image("Yellow Jacket.png"));
			break;
		case "Ironman":
			view = new ImageView(new Image("Iron Man.png"));
			break;
		case "Venom":
			view = new ImageView(new Image("Venom.png"));
			break;
		case "Loki":
			view = new ImageView(new Image("Loki.png"));
			break;
		case "Hela":
			view = new ImageView(new Image("Hela.png"));
			break;
		case "Hulk":
			view = new ImageView(new Image("Hulk.png"));
			break;
		case "Ghost Rider":
			view = new ImageView(new Image("Ghost Rider.png"));
			break;
		case "Iceman":
			view = new ImageView(new Image("Ice Man.png"));
			break;
		case "Spiderman":
			view = new ImageView(new Image("Spider Man.png"));
			break;
		case "Thor":
			view = new ImageView(new Image("Thor.png"));
			break;
		case "Electro":
			view = new ImageView(new Image("electro.png"));
			break;

		}

		return view;
	}

	public void checkGameOveer() {
		Player p = game.checkGameOver();

		if (p != null) {
			Button b = new Button("End Game");
			TextArea txt = new TextArea();
			txt.setText("                   " + p.getName() + "WON!!!!");
			txt.setEditable(false);
			Stage end = new Stage();
			VBox v = new VBox();
			v.getChildren().addAll(txt, b);
			Scene s = new Scene(v, 400, 100);
			end.setScene(s);
			b.setAlignment(Pos.BOTTOM_CENTER);
			b.setOnAction(e -> System.exit(0));
			end.show();
		}

	}

	public void singleTarget() {
		Button[][] buttons = new Button[5][5];
		Stage stage = new Stage();
		TilePane t = new TilePane();
		// int factor = 1;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Damageable d = (Damageable) game.getBoard()[i][j];
				Button b = new Button();
				b.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						for (int i = 0; i < 5; i++) {
							for (int j = 0; j < 5; j++) {
								Button b = (Button) arg0.getSource();
								try {
									if (buttons[i][j] != null && buttons[i][j].equals(b)) {
										game.castAbility(a, i, j);
										updateBoard();
										leftBoxMaker();
										Updateright(info);
										checkGameOveer();
										stage.hide();
									}
								} catch (NotEnoughResourcesException e) {
									errorWindow(e.getMessage());
									stage.hide();
								} catch (AbilityUseException e) {
									errorWindow(e.getMessage());
									stage.hide();
								} catch (InvalidTargetException e) {
									errorWindow(e.getMessage());
									stage.hide();
								} catch (CloneNotSupportedException e) {
									errorWindow(e.getMessage());
									stage.hide();
								}

							}
						}
					}

				});
				if (d == null) {
					b.setText("");
					b.setPrefSize(80, 80);
					t.getChildren().add(b);
					buttons[i][j] = b;
				}
				if (d instanceof Cover) {
					b.setText("Cover");
					b.setPrefSize(80, 80);
					t.getChildren().add(b);
					buttons[i][j] = b;
				}
				if (d instanceof Champion) {
					b.setText(((Champion) d).getName());
					b.setPrefSize(80, 80);
					t.getChildren().add(b);
					buttons[i][j] = b;

				}

			}
		}
		Scene s = new Scene(t, 400, 400);
		stage.setScene(s);
		stage.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
