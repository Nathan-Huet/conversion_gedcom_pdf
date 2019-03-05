package modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import exception.AucunFichierSelectionneException;
import exception.FamilleExistanteException;
import exception.FamilleNonExistanteException;
import modele.infos.Evenement;
import modele.infos.Fichier;
import modele.infos.Genre;
import modele.infos.Lien;
import modele.infos.Nom;
import modele.infos.Note;
import modele.infos.Prenom;
import modele.infos.Profession;
import modele.infos.Surnom;

//COMMENTAIRES A FAIRE
public class Livre {
	private ArrayList<Famille> listeDesFamilles;
	private ArrayList<Personne> listeDesPersonnes;
	private Document livrePDF;
	private PdfTemplate template;
	public String modele = null;
	public String temp = "temporaire.pdf";
	public String pdf = null;
	public String image = null;
	public String page_gauche = null;
	public String page_droite = null;
	public String page_couv_gauche = null;
	public String page_couv_droite = null;
	public String lien_page_suivante = null;
	public String lien_page_precedente = null;

	/**
	 * constructeur de la classe Livre initialise la liste des familles du Livre et
	 * ajoute la famille inconnu a cette liste
	 */
	public Livre() {
		initialiserLivre();
	}

	/**
	 * methode permettant d'obtenir la liste des familles contenues dans le livre
	 * 
	 * @return la liste des familles contenues dans le livre
	 */
	public ArrayList<Famille> getListeDesFamilles() {
		return listeDesFamilles;
	}

	/**
	 * methode permettant d'initialiser ou de reinitialiser le Livre
	 */
	public void initialiserLivre() {
		livrePDF = new Document(PageSize.A4.rotate());
		listeDesFamilles = new ArrayList<Famille>();
		listeDesFamilles.add(new FamilleConcrete("inconnu"));
	}

	/**
	 * methode permettant d'ajouter une famille au Livre a condition que le numero
	 * de la famille ne soit pas deja dans le Livre CF methode equals de la classe
	 * Famille
	 * 
	 * @param famille Famille a ajouter dans le livre
	 * @throws FamilleExistanteException Exception renvoyee lorsque le numero de la
	 *                                   famille a ajouter est deja dans le livre
	 */
	public void ajouterFamille(Famille famille) throws FamilleExistanteException {
		if (listeDesFamilles.contains(famille)) {
			throw new FamilleExistanteException("Cette famille existe dejà dans le livre");
		}
		listeDesFamilles.add(famille);
	}

	/**
	 * methode permettant de recuperer une Famille contenue dans le livre a l'aide
	 * de son numero de famille (unique dans le fichier) CF methode equals de la
	 * classe Famille
	 * 
	 * @param numFamille numero de la Famille cherchee
	 * @return Famille cherchee
	 * @throws FamilleNonExistanteException Exception renvoyee lorsque la Famille
	 *                                      cherchee n'est pas presente dans le
	 *                                      Livre
	 */
	public Famille getFamille(String numFamille) throws FamilleNonExistanteException {
		for (int i = 0; i < listeDesFamilles.size(); i++) {
			if (listeDesFamilles.get(i).getNumFamille().equals(numFamille)) {
				return listeDesFamilles.get(i);
			}
		}
		throw new FamilleNonExistanteException("Cette Famille n'est pas dans le livre");
	}

	/**
	 * methode permettant d'obtenir la Famille parental d'une Personne (la Personne
	 * se trouve dans la liste des enfants de la Famille)
	 * 
	 * @param personne Personne dont on cherche la Famille parental
	 * @return Famille parental de la Personne
	 * @throws FamilleNonExistanteException Exception renvoyee lorsque la Personne
	 *                                      ne possede pas de Famille parental (pas
	 *                                      dans le fichier)
	 */
	public Famille getFamilleParental(Personne personne) throws FamilleNonExistanteException {
		for (Famille famille : listeDesFamilles) {
			if (famille.estDansListeDesEnfant(personne))
				return famille;
		}
		throw new FamilleNonExistanteException("Cette Famille n'est pas dans le livre");
	}

	/**
	 * methode permettant la generation d'un PDF semblable a un livre contenant les
	 * informations de la liste des familles du Livre
	 * 
	 * @throws DocumentException                Exception signalant qu'il y a eu une
	 *                                          erreur dans un Document (source:
	 *                                          http://itextsupport.com "itext
	 *                                          Exception")
	 * @throws FileNotFoundException            Exception signalant qu'un essai
	 *                                          d'ouverture de fichier designe par
	 *                                          un chemin d'acces a echoue. (source:
	 *                                          javadoc FileNotFoundException)
	 * @throws IOException                      Exception signalant qu'une erreur
	 *                                          d'entree/sortie a eu lieu. Cette
	 *                                          classe est la classe generale des
	 *                                          exceptions produites par des
	 *                                          operations d'entree/sortie
	 *                                          interrompues ou echouees. (source:
	 *                                          javadoc IOException)
	 * @throws FamilleNonExistanteException     Exception signalant qu'une Famille
	 *                                          cherchee ne se trouve pas dans le
	 *                                          Livre
	 * @throws AucunFichierSelectionneException Exception signalant qu'il n'y a pas
	 *                                          de fichier selectionne
	 */
	public void creerPDF()
			throws DocumentException, IOException, FamilleNonExistanteException, AucunFichierSelectionneException {
		try {
			creerTemplate();
		} catch (Exception e) {
			System.out.println("Vous n'avez pas choisi de modele");
		}

		FileOutputStream fos = new FileOutputStream(temp);
		PdfWriter writer = PdfWriter.getInstance(livrePDF, fos);

		livrePDF.open();

		try {
			PdfReader reader = new PdfReader(modele);
			ajouterCouverture(writer, reader);
			template = writer.getImportedPage(reader, 2);

			MyPdfPageEventHelper pdfPageEventHelper = new MyPdfPageEventHelper();
			writer.setPageEvent(pdfPageEventHelper);
		} catch (Exception e) {
			System.out.println("Comme le modele n'a pas était choisi le pdf n'en utilisera pas");
		}

		for (int i = 0; i < listeDesFamilles.size(); i++) {
			for (int j = 0; j < listeDesFamilles.get(i).getListeDesEnfants().size(); j++) {
				creerUnePage(listeDesFamilles.get(i).getListeDesEnfants().get(j), writer);
			}
		}

		creerSommaire(writer);
		livrePDF.close();
		ajouterLiensTournerLesPages();
	}

	/**
	 * methode permettant de generer le sommaire
	 * 
	 * @param writer
	 * @throws DocumentException Exception signalant qu'il y a eu une erreur dans un
	 *                           Document (source: http://itextsupport.com "itext
	 *                           Exception")
	 */
	public void creerSommaire(PdfWriter writer) throws DocumentException {
		Font blue = new Font(FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLUE);

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		char lettre[] = alphabet.toCharArray();
		livrePDF.newPage();
		livrePDF.add(Chunk.NEWLINE);

		ArrayList<Paragraph> leSommaire = new ArrayList<>();
		for (int j = 0; j < lettre.length; j++) {
			Paragraph para = new Paragraph();
			Anchor anchorTarget = new Anchor(lettre[j] + "\n", blue);
			anchorTarget.setName(lettre[j] + "");
			para.add(anchorTarget);
			leSommaire.add(para);
		}

		for (Personne personne : listeDesPersonnes) {
			if (personne.getInformation(Nom.KEY).size() > 0) {
				for (int j = 0; j < lettre.length; j++) {
					if (personne.getInformation(Nom.KEY).getFirst().toLowerCase().charAt(0) == lettre[j]) {
						ajouterAnchor(personne, leSommaire.get(j));
					}
				}
			} else {
				ajouterAnchor(personne, leSommaire.get(0));
			}
		}

		for (Paragraph sommaire : leSommaire) {
			livrePDF.add(sommaire);
			livrePDF.newPage();
			livrePDF.add(Chunk.NEWLINE);
		}
	}

	/**
	 * methode permettant la creation d'une page du Livre contenant sur la page de
	 * gauche les informations concernant la Personne uniquement et sur la page de
	 * droite les membres de sa Famille
	 * 
	 * @param famille  Famille de la Personne dont les informations sont renseignees
	 *                 sur la page a creer
	 * @param personne Personne dont les informations sont renseignees sur la page a
	 *                 creer
	 * @param writer
	 * @throws DocumentException            Exception signalant qu'il y a eu une
	 *                                      erreur dans un Document (source:
	 *                                      http://itextsupport.com "itext
	 *                                      Exception")
	 * @throws FamilleNonExistanteException Exception signalant qu'une Famille
	 *                                      cherchee n'est pas dans le Livre
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public void creerUnePage(Personne personne, PdfWriter writer)
			throws DocumentException, FamilleNonExistanteException, MalformedURLException, IOException {
		livrePDF.newPage();
		livrePDF.add(Chunk.NEWLINE);

		PdfPTable pageGaucheDroite = new PdfPTable(2);
		pageGaucheDroite.setWidthPercentage(100);

		if (personne.getInformation(Fichier.KEY).size() > 0 && modele != null) {
			File f = new File(personne.getInformation(Fichier.KEY).get(0) + ".jpg");
			if (f.isFile()) {
				Image img = Image.getInstance(image + personne.getInformation(Fichier.KEY).get(0) + ".jpg");
				img.scaleToFit(100, 150);
				img.setAbsolutePosition(PageSize.A4.getWidth() / 2, PageSize.A4.getHeight() * 0.6f);
				livrePDF.add(img);
			}
		}
		Paragraph paraPageGauche = donneesPersonne(personne);
		Paragraph paraPageDroite = infosFamille(personne);

		PdfPCell pageGauche = new PdfPCell(paraPageGauche);
		pageGauche.setBorder(Rectangle.NO_BORDER);
		PdfPCell pageDroite = new PdfPCell(new Paragraph(paraPageDroite));
		pageDroite.setBorder(Rectangle.NO_BORDER);

		pageGaucheDroite.addCell(pageGauche);
		pageGaucheDroite.addCell(pageDroite);

		livrePDF.add(pageGaucheDroite);

	}

	/**
	 * créer le modèle du livre
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void creerTemplate() throws IOException, DocumentException {

		Document modeleDoc = new Document(livrePDF.getPageSize());
		FileOutputStream fos = new FileOutputStream(modele);
		PdfWriter writer = PdfWriter.getInstance(modeleDoc, fos);
		modeleDoc.open();
		// ajouterCouvertureTemplate(writer);

		ajouterCouvertureTemplate(writer);
		modeleDoc.newPage();
		modeleDoc.add(Chunk.NEWLINE);
		ajouterImagesDeFond(writer);
		// ajouterImageLienTemplate(writer,modeleDoc);
		modeleDoc.close();
	}

	/**
	 * methode permettant d'ajouter les images de fond pour le livre
	 * 
	 * @param writer Une classe DocWriter pour PDF (source:
	 *               http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfWriter.html)
	 * @throws MalformedURLException Exception signalant qu'un URL malforme a ete
	 *                               rencontre. Soit un protocol non autorise a ete
	 *                               trouve dans une chaine de caractere (
	 *                               specification string ), soit la chaine n'a pas
	 *                               pu etre analysee. (source:
	 *                               java.net.MalformedURLException)
	 * @throws IOException           Exception signalant qu'une erreur
	 *                               d'entree/sortie a eu lieu. Cette classe est la
	 *                               classe generale des exceptions produites par
	 *                               des operations d'entree/sortie interrompues ou
	 *                               echouees. (source: javadoc IOException)
	 * @throws DocumentException     Exception signalant qu'il y a eu une erreur
	 *                               dans un Document (source:
	 *                               http://itextsupport.com "itext Exception")
	 */
	public void ajouterCouvertureTemplate(PdfWriter writer)
			throws BadElementException, MalformedURLException, DocumentException, IOException {
		PdfContentByte canvas = writer.getDirectContentUnder();
		if (page_couv_gauche != null)
			canvas.addImage(ajouterImagePageGauche(page_couv_gauche));
		if (page_couv_droite != null)
			canvas.addImage(ajouterImagePageDroite(page_couv_droite));
	}

	public void ajouterCouverture(PdfWriter writer, PdfReader reader) {
		PdfTemplate couverture = writer.getImportedPage(reader, 1);
		ajouterImageTemplate(couverture, writer);
	}

	/**
	 * méthode permettant d'ajouter les images de pages du livre
	 * 
	 * @param image
	 * @param writer
	 */
	public void ajouterImageTemplate(PdfTemplate image, PdfWriter writer) {
		PdfContentByte canvas = writer.getDirectContentUnder();
		canvas.addTemplate(image, 0, -1f, 1f, 0, 0, PageSize.A4.rotate().getHeight());
	}

	/**
	 * methode permettant d'ajouter les images de fond pour le livre
	 * 
	 * @param writer Une classe DocWriter pour PDF (source:
	 *               http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfWriter.html)
	 * @throws MalformedURLException Exception signalant qu'un URL malforme a ete
	 *                               rencontre. Soit un protocol non autorise a ete
	 *                               trouve dans une chaine de caractere (
	 *                               specification string ), soit la chaine n'a pas
	 *                               pu etre analysee. (source:
	 *                               java.net.MalformedURLException)
	 * @throws IOException           Exception signalant qu'une erreur
	 *                               d'entree/sortie a eu lieu. Cette classe est la
	 *                               classe generale des exceptions produites par
	 *                               des operations d'entree/sortie interrompues ou
	 *                               echouees. (source: javadoc IOException)
	 * @throws DocumentException     Exception signalant qu'il y a eu une erreur
	 *                               dans un Document (source:
	 *                               http://itextsupport.com "itext Exception")
	 */
	public void ajouterImagesDeFond(PdfWriter writer) throws MalformedURLException, IOException, DocumentException {
		PdfContentByte canvas = writer.getDirectContentUnder();
		if (page_gauche != null)
			canvas.addImage(ajouterImagePageGauche(page_gauche));
		if (page_droite != null)
			canvas.addImage(ajouterImagePageDroite(page_droite));
	}

	/**
	 * methode renvoyant l'image de fond de la page de gauche
	 * 
	 * @param page_gauche2
	 * @return l'image de fond de la page de gauche
	 * @throws BadElementException   Exception signalant une tentative de création
	 *                               d'un élément qui n'a pas la forme
	 *                               correcte.(source: http://itextsupport.com
	 *                               "itext Exception")
	 * @throws MalformedURLException Exception signalant qu'un URL malforme a ete
	 *                               rencontre. Soit un protocol non autorise a ete
	 *                               trouve dans une chaine de caractere (
	 *                               specification string ), soit la chaine n'a pas
	 *                               pu etre analysee. (source:
	 *                               java.net.MalformedURLException)
	 * @throws IOException           Exception signalant qu'une erreur
	 *                               d'entree/sortie a eu lieu. Cette classe est la
	 *                               classe generale des exceptions produites par
	 *                               des operations d'entree/sortie interrompues ou
	 *                               echouees. (source: javadoc IOException)
	 */
	public Image ajouterImagePageGauche(String page_gauche2)
			throws BadElementException, MalformedURLException, IOException {
		Image image = Image.getInstance(page_gauche2);
		image.scaleAbsoluteWidth(livrePDF.getPageSize().getWidth() / 2);
		image.scaleAbsoluteHeight(livrePDF.getPageSize().getHeight());
		image.setAbsolutePosition(livrePDF.left() - livrePDF.leftMargin(), livrePDF.bottom() - livrePDF.bottomMargin());
		return image;
	}

	/**
	 * methode renvoyant l'image de fond de la page de droite
	 * 
	 * @param page_droite
	 * @return l'image de fond de la page de droite
	 * @throws BadElementException   Exception signalant une tentative de création
	 *                               d'un élément qui n'a pas la forme
	 *                               correcte.(source: http://itextsupport.com
	 *                               "itext Exception")
	 * @throws MalformedURLException Exception signalant qu'un URL malforme a ete
	 *                               rencontre. Soit un protocol non autorise a ete
	 *                               trouve dans une chaine de caractere (
	 *                               specification string ), soit la chaine n'a pas
	 *                               pu etre analysee. (source:
	 *                               java.net.MalformedURLException)
	 * @throws IOException           Exception signalant qu'une erreur
	 *                               d'entree/sortie a eu lieu. Cette classe est la
	 *                               classe generale des exceptions produites par
	 *                               des operations d'entree/sortie interrompues ou
	 *                               echouees. (source: javadoc IOException)
	 */
	public Image ajouterImagePageDroite(String page_droite2)
			throws BadElementException, MalformedURLException, IOException {
		Image image = Image.getInstance(page_droite2);
		image.scaleAbsoluteWidth(livrePDF.getPageSize().getWidth() / 2);
		image.scaleAbsoluteHeight(livrePDF.getPageSize().getHeight());
		image.setAbsolutePosition(livrePDF.getPageSize().getWidth() / 2, 0);
		return image;
	}

	/**
	 * methode permettant la creation du Paragraph a inserer dans la page gauche (
	 * contient les informations privees de la Personne )
	 * 
	 * @param personne Personne dont les informations vont etre affichees en page
	 *                 gauche
	 * @return Paragraph qui va etre affiche en page gauche ( contient les
	 *         informations privees de la Personne )
	 */
	public Paragraph donneesPersonne(Personne personne) {
		Paragraph donnee = new Paragraph();

		ajouterAnchorTarget(personne, donnee);
		// Creation de la reference permettant de cibler la personne
		//

		if (personne.getInformation(Prenom.KEY).size() >= 1) {
			donnee.add("Cette personne porte les prénoms suivants:\n");
			for (String prenom : personne.getInformation(Prenom.KEY)) {
				donnee.add(prenom + "\n");
			}
		} else {
			donnee.add("Le prénom de cette personne est inconnu \n");
		}
		donnee.add("\n");

		if (personne.getInformation(Nom.KEY).size() >= 1) {
			donnee.add("Cette personne porte les noms de familles suivants:\n");
			for (String nom : personne.getInformation(Nom.KEY)) {
				donnee.add(nom + "\n");
			}
		} else {
			donnee.add("Le nom de cette personne est inconnu \n");
		}
		donnee.add("\n");

		donnee.add("Cette personne est du genre: ");

		if (personne.getInformation(Genre.KEY).isEmpty()) {
			donnee.add("inconnu");
		} else {
			switch (personne.getInformation(Genre.KEY).getFirst().toUpperCase().substring(0, 1)) {
			case "M":
				donnee.add("masculin");
				break;
			case "F":
				donnee.add("féminin");
				break;
			default:
				donnee.add("inconnu");
				break;
			}
		}
		donnee.add("\n");
		donnee.add("\n");

		if (personne.getInformation(Surnom.KEY).size() >= 1) {
			donnee.add("Cette personne porte les surnoms suivants:\n");
			for (String surnom : personne.getInformation(Surnom.KEY)) {
				donnee.add(surnom + "\n");
			}
		}
		donnee.add("\n");

		if (personne.getInformation(Profession.KEY).size() >= 1) {
			donnee.add("Cette personne a exercé les professions suivantes:\n");
			for (String profession : personne.getInformation(Profession.KEY)) {
				donnee.add(profession + "\n");
			}
		} else {
			donnee.add("Les professions exercées par cette personne sont inconnues \n");
		}
		donnee.add("\n");

		if (personne.getInformation(Note.KEY).size() >= 1) {
			donnee.add("Les informations particulières concernant cette personne sont: \n");
			for (String notes : personne.getInformation(Note.KEY)) {
				donnee.add(notes + "\n");
			}
		}
		donnee.add("\n");

		if (personne.getInformation(Evenement.KEY).size() >= 1) {
			donnee.add("Les evenements connus concernant cette personne sont: \n");
			for (String evenements : personne.getInformation(Evenement.KEY)) {
				donnee.add(evenements + "\n");
			}
		}
		donnee.add("\n");

		if (personne.getInformation(Lien.KEY).size() >= 1) {
			donnee.add("Les liens concernant cette personne sont: \n");
			for (String lien : personne.getInformation(Lien.KEY)) {
				Anchor anchor = new Anchor(lien + "\n");
				anchor.setReference(lien);
				donnee.add(lien + "\n");
			}
		}
		donnee.add("\n");

		return donnee;
	}

	/**
	 * methode permettant la creation du Paragraph a inserer dans la page droite (
	 * contient les informations sur la Famille de la Personne )
	 * 
	 * @param personne Personne dont les informations familiales vont etre affichees
	 *                 en page droite
	 * @return Paragraph qui va etre affiche en page droite ( contient les
	 *         informations sur la Famille de la Personne )
	 * @throws FamilleNonExistanteException propage une exception si la Famille
	 *                                      cherchee n'est pas dans le Livre
	 */
	public Paragraph infosFamille(Personne personne) throws FamilleNonExistanteException {
		Paragraph paragraphe = new Paragraph();
		Phrase phrase = new Phrase();

		if (getFamilleParental(personne).getNumFamille().equals("inconnu")) {
			phrase.add("Les parents et les frères et soeurs de cette personne sont inconnus\n");
		} else {
			phrase.add("Les parents de cette personne sont :\n");
		}
		paragraphe.add(phrase);

		ajouterAnchor(getFamilleParental(personne).getParent_1(), paragraphe);

		ajouterAnchor(getFamilleParental(personne).getParent_2(), paragraphe);

		if (!getFamilleParental(personne).getNumFamille().equals("inconnu")) {
			phrase = new Phrase();
			phrase.add("Les frères et soeurs de cette personne sont : \n");
			if (getFamilleParental(personne).getListeDesEnfants().size() == 0)
				phrase.add("INCONNUS \n");
			paragraphe.add(phrase);

			for (Personne enfant : getFamilleParental(personne).getListeDesEnfants()) {
				if (enfant != personne)
					ajouterAnchor(enfant, paragraphe);
			}
		}

		phrase = new Phrase();
		phrase.add("Les enfants de cette personne sont : \n");
		paragraphe.add(phrase);
		for (Famille familleI : listeDesFamilles) {
			if (familleI.estUnParentDeLaFamille(personne)) {
				for (Personne enfant : familleI.getListeDesEnfants()) {
					ajouterAnchor(enfant, paragraphe);
				}
			}
		}

		return paragraphe;
	}

	/**
	 * methode permettant d'ajouter les liens pour aller aux pages suivantes et
	 * precedentes du Livre.
	 * 
	 * @throws FileNotFoundException            Exception signalant qu'un essai
	 *                                          d'ouverture de fichier designe par
	 *                                          un chemin d'acces a echoue.(source:
	 *                                          javadoc FileNotFoundException)
	 * @throws DocumentException                Exception signalant qu'il y a eu une
	 *                                          erreur dans un Document (source:
	 *                                          http://itextsupport.com "itext
	 *                                          Exception")
	 * @throws IOException                      Exception signalant qu'une erreur
	 *                                          d'entree/sortie a eu lieu. Cette
	 *                                          classe est la classe generale des
	 *                                          exceptions produites par des
	 *                                          operations d'entree/sortie
	 *                                          interrompues ou echouees. (source:
	 *                                          javadoc IOException)
	 * @throws AucunFichierSelectionneException Exception signalant qu'il n'y a pas
	 *                                          de fichier selectionne
	 */
	public void ajouterLiensTournerLesPages()
			throws FileNotFoundException, DocumentException, IOException, AucunFichierSelectionneException {
		PdfReader reader = new PdfReader(temp);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdf));
		if (lien_page_suivante != null && lien_page_precedente != null) {
			for (int i = 1; i < reader.getNumberOfPages() + 1; i++) {
				ajouterLien(reader, stamper, i);
			}
		}
		stamper.close();
	}

	/**
	 * methode permettant d'ajouter les liens pour aller a la pages suivante et a la
	 * page precedente de la page actuelle (numPage).
	 * 
	 * @param reader  lit un document PDF (source:
	 *                http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfReader.html)
	 * @param stamper applique un contenu supplementaire aux pages d'un document
	 *                PDF. Ce contenu supplementaire peut etre n'importe quel objet
	 *                autorise dans PdfContentByte, y compris des pages d'autres
	 *                PDFs. Le PDF de depart conservera tous les elements
	 *                interactifs, y compris les marque-pages, liens et champs de
	 *                formulaires (source:
	 *                http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfStamper.html)
	 * @param numPage numero de la page sur laquelle il faut ajouter les liens
	 * @throws MalformedURLException            Exception signalant qu'un URL
	 *                                          malforme a ete rencontre. Soit un
	 *                                          protocol non autorise a ete trouve
	 *                                          dans une chaine de caractere (
	 *                                          specification string ), soit la
	 *                                          chaine n'a pas pu etre analysee.
	 *                                          (source:
	 *                                          java.net.MalformedURLException)
	 * @throws IOException                      Exception signalant qu'une erreur
	 *                                          d'entree/sortie a eu lieu. Cette
	 *                                          classe est la classe generale des
	 *                                          exceptions produites par des
	 *                                          operations d'entree/sortie
	 *                                          interrompues ou echouees. (source:
	 *                                          javadoc IOException)
	 * @throws DocumentException                Exception signalant qu'il y a eu une
	 *                                          erreur dans un Document (source:
	 *                                          http://itextsupport.com "itext
	 *                                          Exception")
	 * @throws AucunFichierSelectionneException Exception signalant qu'il n'y a pas
	 *                                          de fichier selectionne
	 */
	public void ajouterLien(PdfReader reader, PdfStamper stamper, int numPage)
			throws MalformedURLException, IOException, DocumentException, AucunFichierSelectionneException {
		Image img_page_s = Image.getInstance(lien_page_suivante);
		Image img_page_p = Image.getInstance(lien_page_precedente);
		img_page_s.scaleAbsolute(reader.getPageSize(1).getWidth() / 20, reader.getPageSize(1).getHeight() / 20);
		img_page_p.scaleAbsolute(reader.getPageSize(1).getWidth() / 20, reader.getPageSize(1).getHeight() / 20);
		if (numPage > 1)
			ajouterLienPagePrecedente(img_page_p, reader, stamper, numPage);
		if (numPage < reader.getNumberOfPages())
			ajouterLienPageSuivante(img_page_s, reader, stamper, numPage);
	}

	/**
	 * methode permettant d'ajouter le liens pour aller a la page precedente de la
	 * page actuelle (numPage).
	 * 
	 * @param img     image a afficher a l'endroit du lien
	 * @param reader  lit un document PDF (source:
	 *                http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfReader.html)
	 * @param stamper applique un contenu supplementaire aux pages d'un document
	 *                PDF. Ce contenu supplementaire peut etre n'importe quel objet
	 *                autorise dans PdfContentByte, y compris des pages d'autres
	 *                PDFs. Le PDF de depart conservera tous les elements
	 *                interactifs, y compris les marque-pages, liens et champs de
	 *                formulaires (source:
	 *                http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfStamper.html)
	 * @param numPage numero de la page sur laquelle il faut ajouter les liens
	 * @throws DocumentException Exception signalant qu'il y a eu une erreur dans un
	 *                           Document (source: http://itextsupport.com "itext
	 *                           Exception")
	 */
	public void ajouterLienPagePrecedente(Image img, PdfReader reader, PdfStamper stamper, int numPage)
			throws DocumentException {
		float w = img.getScaledWidth();
		float h = img.getScaledHeight();
		float x = livrePDF.left() - livrePDF.leftMargin();
		float y = livrePDF.bottom() - livrePDF.bottomMargin();

		img.setAbsolutePosition(x, y);
		stamper.getOverContent(numPage).addImage(img);
		Rectangle linkLocation = new Rectangle(x, y, x + w, y + h);
		PdfDestination destination = new PdfDestination(PdfDestination.FIT);
		PdfAnnotation link = PdfAnnotation.createLink(stamper.getWriter(), linkLocation,
				PdfAnnotation.HIGHLIGHT_OUTLINE, numPage - 1, destination);
		link.setBorder(new PdfBorderArray(0, 0, 0));
		stamper.addAnnotation(link, numPage);
	}

	/**
	 * methode permettant d'ajouter le liens pour aller a la page suivante de la
	 * page actuelle (numPage).
	 * 
	 * @param img     image a afficher a l'endroit du lien
	 * @param reader  lit un document PDF (source:
	 *                http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfReader.html)
	 * @param stamper applique un contenu supplementaire aux pages d'un document
	 *                PDF. Ce contenu supplementaire peut etre n'importe quel objet
	 *                autorise dans PdfContentByte, y compris des pages d'autres
	 *                PDFs. Le PDF de depart conservera tous les elements
	 *                interactifs, y compris les marque-pages, liens et champs de
	 *                formulaires (source:
	 *                http://itextsupport.com/apidocs/itext5/5.5.9/com/itextpdf/text/pdf/PdfStamper.html)
	 * @param numPage numero de la page sur laquelle il faut ajouter les liens
	 * @throws DocumentException Exception signalant qu'il y a eu une erreur dans un
	 *                           Document (source: http://itextsupport.com "itext
	 *                           Exception")
	 */
	public void ajouterLienPageSuivante(Image img, PdfReader reader, PdfStamper stamper, int numPage)
			throws DocumentException {
		float w = img.getScaledWidth();
		float h = img.getScaledHeight();
		float x = livrePDF.right() + livrePDF.rightMargin();
		float y = livrePDF.bottom() - livrePDF.bottomMargin();

		img.setAbsolutePosition(x - w, y);
		stamper.getOverContent(numPage).addImage(img);
		Rectangle linkLocation = new Rectangle(x, y, x - w, y + h);
		PdfDestination destination = new PdfDestination(PdfDestination.FIT);
		PdfAnnotation link = PdfAnnotation.createLink(stamper.getWriter(), linkLocation,
				PdfAnnotation.HIGHLIGHT_OUTLINE, numPage + 1, destination);
		link.setBorder(new PdfBorderArray(0, 0, 0));
		stamper.addAnnotation(link, numPage);

	}

	/**
	 * methode permettant d'ajouter une destination de lien (lorsque l'on clique sur
	 * un lien celui- dirige vers cette destination)
	 * 
	 * @param personne   cible du lien
	 * @param paragraphe paagraphe contenant la destination du lien
	 */
	public void ajouterAnchorTarget(Personne personne, Paragraph paragraphe) {
		Anchor anchorTarget = new Anchor(personne.toString() + "\n");
		anchorTarget.setName(personne.getNum());
		paragraphe.add(anchorTarget);
	}

	/**
	 * methode permettant d'ajouter un lien vers le numero d'une Personne
	 * 
	 * @param personne   personne ciblee par le lien
	 * @param paragraphe paragraphe contenant le lien
	 */
	public void ajouterAnchor(Personne personne, Paragraph paragraphe) {
		Anchor anchor = new Anchor(personne.toString() + "\n");
		anchor.setReference("#" + personne.getNum());
		paragraphe.add(anchor);
	}

	/**
	 * permet de gérer les évènements exemple: le début de chaque page
	 */
	public class MyPdfPageEventHelper extends PdfPageEventHelper {
		/**
		 * ajoute le marque page vers le sommaire et les images de pages du livre en
		 * fond
		 */
		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			super.onStartPage(writer, document);
			if (template != null) {
				ajouterImageTemplate(template, writer);
			}

			String alphabet = "abcdefghijklmnopqrstuvwxyz";
			char lettre[] = alphabet.toCharArray();
			Paragraph para = new Paragraph();
			for (char c : lettre) {
				Anchor anchor = new Anchor(c + " ");
				anchor.setReference("#" + c);
				para.add(anchor);
			}

			try {
				document.add(para);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * trie la liste des personnes
	 * 
	 * @param toutesPersonne
	 */
	public void trier(ArrayList<Personne> toutesPersonne) {
		toutesPersonne.sort(null);
		listeDesPersonnes = toutesPersonne;
	}
}
