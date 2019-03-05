package modele.infos;

import java.util.LinkedList;

public class Evenement extends Information {
	public final static String KEY = "evenements";

	public Evenement() {
		listeInformation = new LinkedList<String>();
	}

	@Override
	public void ajouterInformation(String information) {
		String type[] = information.split("\\s+");
		String texte = "";
		String temp[];
		information = "";
		for (int i = 1; i < type.length; i++) {
			texte += type[i] + " ";
		}

		switch (type[0]) {
		case "BIRT":
			listeInformation.add("Cette Personne est nee ");
			break;
		case "DEAT":
			listeInformation.add("Cette Personne est morte ");
			break;
		case "EVEN":
			listeInformation.add("Il s'est passe l'evenement suivant ");
			break;
		case "PLAC":
			listeInformation.add("Dans le lieu suivant ");
			temp = texte.split(",");
			for (int i = 0; i < temp.length; i++) {
				if (!temp[i].isEmpty()) {

					information += temp[i];
					if (i == 0)
						information += " (";

					else if (i < temp.length - 2)
						information += ", ";
				}
			}
			information += ")";
			break;
		case "DATE":
			listeInformation.add("A la date suivante ");
			temp = texte.split("\\s+");

			for (int i = 0; i < temp.length; i++) {
				switch (temp[i]) {
				case "@#DJULIAN@":
					information += "(Calendrier Julien) ";
					break;
				case "ABT":
					information += "Aux alentours de ";
					break;
				case "JAN":
					information += "Janvier ";
					break;
				case "FEB":
					information += "Fevrier ";
					break;
				case "MAR":
					information += "Mars ";
					break;
				case "APR":
					information += "Avril ";
					break;
				case "MAY":
					information += "Mai ";
					break;
				case "JUN":
					information += "Juin ";
					break;
				case "JUL":
					information += "Juillet ";
					break;
				case "AUG":
					information += "Aout ";
					break;
				case "SEP":
					information += "Septembre ";
					break;
				case "OCT":
					information += "Octobre ";
					break;
				case "NOV":
					information += "Novembre ";
					break;
				case "DEC":
					information += "Decembre ";
					break;
				default:
					information += temp[i] + " ";
					break;
				}
			}
			break;
		default:
			temp = information.split("\\s+");
			// information = "";
			// listeInformation.add(information);
			break;
		}
		if (!information.isEmpty())
			listeInformation.add(information);
	}
	
}
