package appli;

import controleur.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Livre;
import vue.Fenetre;

public class Appli extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Livre livre = new Livre();
		Fenetre fenetre = new Fenetre(livre);
		Controleur ctrl = new ControleurConcret(livre, fenetre);
		fenetre.controleur(ctrl);
		Scene scene = new Scene(fenetre);
		stage.setScene(scene);
		stage.show();

		/*
		 * 
		 * 
		 * 
		 * Scanner sc = new Scanner(System.in);
		 * System.out.println("Donner le nom d'un fichier gedcom"); File fichier = new
		 * File(sc.next()+".GED");
		 * 
		 * 
		 * try { traiteFich.enregistrerIndividus(fichier); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (DejaDansFamilleException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (FamilleExistanteException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (FamilleNonExistanteException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * //System.out.println(livre.getListeDesFamilles()); try { livre.creerPDF(); }
		 * catch (DocumentException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (FamilleNonExistanteException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * sc.close();
		 * 
		 * 
		 * 
		 */

	}

}
