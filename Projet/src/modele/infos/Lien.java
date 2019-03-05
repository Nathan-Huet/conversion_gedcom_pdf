package modele.infos;

import java.util.LinkedList;

public class Lien extends Information {
	public final static String KEY = "liens";

	public Lien() {
		listeInformation = new LinkedList<String>();
	}
}
