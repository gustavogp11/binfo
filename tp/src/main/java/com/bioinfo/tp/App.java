package com.bioinfo.tp;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;

public class App {

    public static void main(String[] args) throws Exception {
        InputStream fastaFile = App.class.getResourceAsStream("/ej.fasta");
        //Try with the FastaReaderHelper
        LinkedHashMap<String, ProteinSequence> a = FastaReaderHelper.readFastaProteinSequence(fastaFile);
        //FastaReaderHelper.readFastaDNASequence for DNA sequences
        for (Entry<String, ProteinSequence> entry : a.entrySet()) {
            System.out.println(entry.getValue().getOriginalHeader() + "=" + entry.getValue().getSequenceAsString());
        }
    }
}
