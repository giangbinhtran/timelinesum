/**
 * format a news following some steps:
 * 1. Remove non-ascii character
 * 2. Split sentences
 * 3. Remove too short sentences (number of words <4)
 */
package wksum.news;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import wksum.util.Common;
import wksum.util.Stopwords;

public class NewsFormator {
	 protected HashSet<String> m_bad = null;
	 protected static NewsFormator m_format;

	  static {
	    if (m_format == null) {
	      m_format = new NewsFormator();
	    }
	  }
	  
	  public NewsFormator() {
		  m_bad = new HashSet<String>();
		  m_bad.add("stm USER_NAME = repman DOCUMENT_NAME");
		  m_bad.add("co. uk navigation blq_lang_ss = Skip to bbc .");
		  m_bad.add("co. uk FIDDLER_VERSION = 5.0.0 HTTP_USER_AGENT =");
		  m_bad.add("co. uk HTTP_X_FORWARDED_SERVER = news .");
		  m_bad.add("While you will be able to view the content of this page in your current browser , you will not be able to get the full visual experience .");
		  m_bad.add("Please consider upgrading your browser software or enabling style sheets -LRB- CSS -RRB- if you are able to do so .");
		  m_bad.add("co. uk search blq_lang_ak = Access keys help blq_lang_ak_u");
		  m_bad.add("blq_lang_css = This page is best viewed in an up-to-date web browser with style sheets -LRB- CSS -RRB- enabled .");
		  m_bad.add("co. uk\\/bbc\\/bbc \\ \\/ s ?");
		  m_bad.add("Sign up for free e-mail news alerts");
		  m_bad.add("Letters for publication should be sent to :");
		  m_bad.add("co. uk at");
		  m_bad.add("If you see a comment that you believe is irrelevant or inappropriate , you can flag it to our editors by using the report abuse links .");
		  m_bad.add("-LRB- Additional reporting by ");
		  m_bad.add("-LRB- Writing by ");
		  m_bad.add("Explore the three plans to stop the oil flow .");
		  m_bad.add("See the step-by-step procedure involved in an oil burn .");
		  m_bad.add("This Story : Read + ");
		  m_bad.add("Posted by : ");
		  m_bad.add("on.cnn.com");
		  m_bad.add("We ve seen this movie");
		  m_bad.add("THIS COPY MAY NOT BE IN ITS FINAL FORM AND MAY BE UPDATED .");
		  m_bad.add("stm storyID = ");
		  m_bad.add("stm SCRIPT_NAME = ");
		  m_bad.add("SCRIPT_URL = ");
		  m_bad.add("Please consider upgrading your browser software");
		  m_bad.add("co. uk search blq_lang_ak");
		  m_bad.add("It was last modified at");
		  m_bad.add("It was first published at");
		  m_bad.add("This article was amended");
		  m_bad.add("\\* McCain");
		  m_bad.add("Copyright ");
		  m_bad.add(">> reporter :");
		  m_bad.add("phil lebeau");
		  m_bad.add("UNIDENTIFIED MALE :");
		  m_bad.add("COSTAS : ");
		  m_bad.add("ARMSTRONG : ");
		  m_bad.add("KING : ");
		  m_bad.add("BOB COSTAS , CNN CONTRIBUTOR :");
		  m_bad.add("Pay service with live and archived video");
		  m_bad.add("Terms under which this service is provided to you");
		  m_bad.add("A Time Warner Company");
		  m_bad.add("Aired ");
		  m_bad.add("This material may not be published , broadcast , rewritten or redistributed .");
		  m_bad.add("This image contains graphic content that some viewers may find disturbing .");
	  }
	  
	/**
	 * removing the error sentence
	 * @param tokens: tokens of the sentence
	 * @return true/false
	 */
	public static boolean noParsingError(String[] tokens) {
		
		int count =0;
		for (String tk: tokens) {
			if (tk.equalsIgnoreCase("http") || tk.equals("/") || tk.equals("\\/") || tk.equals("\\") || tk.equals("www") || tk.equals("=")) count ++;
		}
		if (count <4) 
			return true;
		else return false;
		
		
	}
	
	public static boolean noParsingError(String s) {
		for (String bad: m_format.m_bad) {
			if (s.startsWith(bad)) return false;
		}
		return true;
	}
	/**
	 * for mat the input news
	 * @param fileloc
	 * @param dest
	 * @throws IOException
	 */
	public static void formatText(String fileloc, String dest) throws IOException {
		File dr = new File (fileloc);
		if (dr.isDirectory()) new File(dest).mkdir();
		if (dr.isFile() && dr.getName().endsWith(".htm.txt")) {
			File destDir = new File(dest);
			//process this news
			System.out.println(dr.getAbsolutePath());
			String output = dest;
			BufferedWriter out = new BufferedWriter(new FileWriter(output));
			Scanner sc = new Scanner(dr);
			StringBuilder content = new StringBuilder();
			while (sc.hasNext()) {
				String s = sc.nextLine();
				String[] tmp = s.split("\\s+");
				if (tmp.length>4 && noParsingError(s) && noParsingError(tmp)) {
					content.append(s + "\n");
				}
			}
			
			ArrayList<String> sentences = Common.sentenceTokenize(content.toString());
			for (String s: sentences) {
				String normalizedS = Common.sentenceNormalize(s);
				String [] tmp = normalizedS.split("\\s+");
				if (tmp.length >4 && noParsingError(normalizedS) && noParsingError(tmp) )
					out.write(String.format("%s\n", normalizedS));
			}
			out.close();
		}
		else if (dr.isDirectory()) {
			for (File f: dr.listFiles()) {
				formatText(f.getAbsolutePath(), dest + "/" + f.getName());
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		HashMap<String, Integer> d = new HashMap<String, Integer>();
		d.put("2010Chile-earthquake",6);
		d.put("2010Haiti-earthquake",6);
		d.put("Bahraini-uprising", 5);
		d.put("BPOil-spill",3);
		d.put("Burmese-anti-government-protests",10);
		d.put("Fukushima-nuclear-disaster",5);
		d.put("Gaza-War", 18);
		d.put("H1N1",3);
		d.put("Hurricane-Katrina",8);
		d.put("Iranian-election-protests-2009",6);
		d.put("Iranian-protests-2011",6);
		d.put("Iraq-invasion-2003",7);
		d.put("Lebanon-War",33);
		d.put("Libyan-civil-war",6);
		d.put("Mexican-Drug-War", 2);
		d.put("Muhammad-cartoons",6);
		d.put("petfood-recalls",3);
		d.put("SaudiArabian-protests",2);
		d.put("shooting-TrayvonMartin",2);
		d.put("South-Ossetia-War",15);
		d.put("Syrian-civil-war",5);
		d.put("War-in-Somalia",3);
		d.put("Yemeni-revolution",3);
		d.put("bpoil_bbc",3);
		d.put("bpoil_foxnews", 5);
		d.put("bpoil_guardian", 3);
		d.put("bpoil_reuters", 2);
		d.put("bpoil_washingtonpost", 2);
		d.put("Finan_bbc", 2);
		d.put("Finan_guardian", 6);
		d.put("Finan_washingtonpost", 7);
		d.put("H1N1_bbc", 5);
		d.put("H1N1_foxnews", 5);
		d.put("H1N1_guardian", 3);
		d.put("H1N1_reuters", 2);
		d.put("haiti_bbc", 8);
		d.put("MJ_bbc", 3);
		d.put("MJ_foxnews", 5);
		d.put("MJ_guardian", 3);
		d.put("MJ_reuters", 2);
		d.put("MJ_washingtonpost", 2);
		
		d.put("ToyotaRecall_nbcnews", 2);
		d.put("SyrianCrisis_reuters", 2);
		d.put("SyrianCrisis_bbc", 3);
		d.put("LibyaWar_reuters", 2);
		d.put("LibyaWar_guardian", 2);
		d.put("LibyaWar_cnn", 3);
		d.put("Katrina_cnn", 6);
		d.put("IraqWar_guardian", 3);
		d.put("EgyptianProtest_cnn", 3);
		
		for (String t: d.keySet()) {
			if (! new File("/workspaces/wksum-project/Data/InternetData/" + t + "/InputDocs/").exists()) continue;
			if (!t.equals("ToyotaRecall_nbcnews"))
			System.out.println(t);
			
			String loc = "/workspaces/wksum-project/Data/InternetData/" + t + "/InputDocs/";
			String dest = "/workspaces/wksum-project/Data/InternetData/" + t + "/InputDocsx/";
			formatText(loc, dest);
		}
	}
}
