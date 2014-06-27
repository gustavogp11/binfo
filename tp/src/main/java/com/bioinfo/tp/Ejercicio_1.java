package com.bioinfo.tp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import org.biojava3.core.sequence.AccessionID;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import org.biojava3.core.sequence.io.GenbankReaderHelper;
import org.biojava3.core.sequence.transcription.Frame;

/**
 *
 * @author Gustavo
 */
public class Ejercicio_1 {
    
    /**
     * 
     * @param genbankFile: archivo en formato genbank
     * @param outputFile: nombre del archivo de salida
     * @return Secuencia de aminoacidos en formato FASTA
     * @throws Exception 
     */     
    public File nucleotidosToAminoacidos(InputStream genbankFile, String outputFile) throws Exception {
        LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(genbankFile);
        File output = new File(outputFile);
        Collection<ProteinSequence> listProteinSequence = new ArrayList<ProteinSequence>();
        for (DNASequence sequence : dnaSequences.values()) {
            for(Frame frame : new Frame[] { Frame.ONE, Frame.TWO, Frame.THREE, Frame.REVERSED_THREE, Frame.REVERSED_TWO, Frame.REVERSED_ONE } ) {
                ProteinSequence seq = sequence.getRNASequence(frame).getProteinSequence();
                seq.setOriginalHeader("FRAME " + frame.name());
                listProteinSequence.add(seq);
            }
	}
        FastaWriterHelper.writeProteinSequence(output, listProteinSequence);
        return output;
    }
    
    private static void checkArgs(String[] args) {
        if(args == null || (args.length < 2))
            throw new RuntimeException("Debe ingresar 1- ruta y nombre completo del archivo input en formato GenBank. "
                    + "2- ruta y nombre completo del archivo output (fasta)");
    }
    
    public static void main(String[] args) throws Exception {
        try {
            /*
            args = new String[] { "C:\\Users\\Gustavo\\Documents\\GitHub\\binfo\\tp\\src\\main\\resources\\nucleotidos-ch6.gb", 
                                "C:\\Users\\Gustavo\\Documents\\Sistemas\\Bioinformatica\\TP\\test.fasta"};
            */
            checkArgs(args);
            InputStream input = new FileInputStream(args[0].toString());
            String outputFile = args[1].toString();
            Ejercicio_1 p = new Ejercicio_1();
            File fout = p.nucleotidosToAminoacidos(input, outputFile);
            System.out.println("Se ha generado el archivo de salida en: " + fout.getPath());
        } catch(Exception e) {
            System.out.println("Error (" + e + "): " + e.getMessage());
        }
    }
    
    


    
}
