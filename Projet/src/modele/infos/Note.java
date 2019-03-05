package modele.infos;

import java.util.LinkedList;

public class Note extends Information {
	public final static String KEY = "notes";

	public Note() {
		listeInformation = new LinkedList<String>();
	}
}
