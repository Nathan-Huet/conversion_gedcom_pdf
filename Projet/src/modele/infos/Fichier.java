package modele.infos;

import java.util.LinkedList;

public class Fichier extends Information {
	public final static String KEY = "Fichiers";

	public Fichier() {
		listeInformation = new LinkedList<String>();
	}
}
