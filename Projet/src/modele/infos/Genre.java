package modele.infos;

import java.util.LinkedList;

public class Genre extends Information {
	public final static String KEY = "genres";

	public Genre() {
		listeInformation = new LinkedList<String>();
	}
}
