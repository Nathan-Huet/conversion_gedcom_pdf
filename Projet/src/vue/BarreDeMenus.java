package vue;

import controleur.Controleur;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class BarreDeMenus extends MenuBar {
	private Controleur ctrl;

	public BarreDeMenus() {
		super();
		Menu m = ajouteMenu("Fichier GedCom");
		ajouteElement("Selectionner", m).setOnAction(evt -> ctrl.selectionner());
		ajouteSeparateur(m);

		m = ajouteMenu("PDF");
		ajouteElement("Generer le PDF", m).setOnAction(evt -> ctrl.genererPDF());
		ajouteElement("Choisir le modele", m).setOnAction(evt -> ctrl.choisirModele());
		ajouteSeparateur(m);

		// ToggleGroup outils = new ToggleGroup();

		m = ajouteMenu("Genealogie");
		ajouteElement("A propos", m).setOnAction(evt -> ctrl.aPropos());
		ajouteElement("Quitter", m).setOnAction(evt -> ctrl.quitter());

	}

	private Menu ajouteMenu(String nom) {
		Menu m = new Menu(nom);
		getMenus().add(m);
		return m;
	}

	private MenuItem ajouteElement(String nom, Menu m) {
		MenuItem mi = new MenuItem(nom);
		m.getItems().add(mi);
		return mi;
	}

	/*
	 * private RadioMenuItem ajouteRadioElement(String nom, Menu m, ToggleGroup g) {
	 * RadioMenuItem mi = new RadioMenuItem(nom); m.getItems().add(mi);
	 * mi.setToggleGroup(g); return mi; }
	 */
	private void ajouteSeparateur(Menu m) {
		SeparatorMenuItem s = new SeparatorMenuItem();
		m.getItems().add(s);
	}

	public void controleur(Controleur controleur) {
		this.ctrl = controleur;
	}
}
