package com.amazonaws.lambda.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class convert {

	public static OutputStream wordToPdf(InputStream inputStream, OutputStream outputStream)
			throws XWPFConverterException, IOException {
		System.out.println("Start wordToPdf");
		XWPFDocument doc = new XWPFDocument(inputStream);
		PdfOptions options = PdfOptions.create();
		PdfConverter.getInstance().convert(doc, outputStream, options);
		System.out.println("doneeeee");
		return outputStream;
	}

	public static OutputStream pdfToWord(InputStream inputStream, OutputStream outputStream) throws IOException {
		System.out.println("Start pdfToWord");
		XWPFDocument doc = new XWPFDocument();
		PdfReader pdfReader = new PdfReader(inputStream);
		PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
		for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
			TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			String text = strategy.getResultantText();
			XWPFParagraph p = doc.createParagraph();
			XWPFRun run = p.createRun();
			run.setText(text);
			run.addBreak(BreakType.PAGE);
		}
		doc.write(outputStream);
		
		pdfReader.close();
		System.out.println("doneeeee");
		return outputStream;
	}

}
