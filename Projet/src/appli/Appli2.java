package appli;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfGraphics2D;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;

public class Appli2 {

	public static void main(String[] args) throws DocumentException, IOException {
		Appli2 app = new Appli2();
		app.lancer();
	}

	public void lancer() throws DocumentException, IOException {
		Document livrePDF = new Document(PageSize.A4.rotate());
		
		FileOutputStream fos = new FileOutputStream("lennui.pdf");
		PdfWriter writer = PdfWriter.getInstance(livrePDF, fos);	
		livrePDF.open();
		
		livrePDF.newPage();
		livrePDF.add(Chunk.NEWLINE);
		Paragraph par = new Paragraph("TEST JS");
		livrePDF.add(par);
		Rectangle rect = new Rectangle(PageSize.A6);
		rect.setBorderColor(BaseColor.GREEN);
		rect.setBorderWidth(20);
		rect.enableBorderSide(1);
		rect.setBackgroundColor(BaseColor.BLUE);
		livrePDF.add(rect);
		livrePDF.close();
		
		PdfStamper stamper = new PdfStamper(new PdfReader("lennui.pdf"), new FileOutputStream("exemple.pdf"));
		//String code = "var test = 5;function test() {app.alert('test');}test();";
		//String code2 = "test();";
		String code = "app.alert('test');";
		
		writer = stamper.getWriter();
		PdfAction action = PdfAction.javaScript(code, writer);
		//PdfAction action2 = PdfAction.javaScript(code2, writer);
		writer.addJavaScript(action);
		
		PdfAnnotation link = new PdfAnnotation(writer, rect);
		link.setAction(action);
		link.setColor(BaseColor.GREEN);
		//link.setHighlighting(PdfName.HIGHLIGHT);
		stamper.addAnnotation(link, 1);
		
		PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(PageSize.A6), "fieldName");	
		button.setText("TEST");
		button.setTextColor(BaseColor.BLACK);
		button.setRotation(90);
		PdfAnnotation annot = button.getField();
		
		//annot.setAction(action2);
		annot.setAdditionalActions(PdfName.E, action);
		
		
		stamper.addAnnotation(annot, 1);
		stamper.close();
		System.out.println("FIN");	
	}

}
