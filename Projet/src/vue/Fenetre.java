package vue;

import controleur.Controleur;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import modele.Livre;

public class Fenetre extends BorderPane {
	private BarreDeMenus bM;
	private TextArea zoneTexte;

	public Fenetre(Livre livre) {
		bM = new BarreDeMenus();
		setTop(bM);
		zoneTexte = new TextArea();
		setCenter(zoneTexte);
	}

	public void controleur(Controleur c) {
		bM.controleur(c);
	}

	public void zoneDeTexte(String texte) {
		zoneTexte.clear();
		zoneTexte.appendText(texte);
	}

}
