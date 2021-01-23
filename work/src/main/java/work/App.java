package work;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Web scraping
 *
 */
public class App 
{
    static int MAX_CNT = 30;
    static int currentCnt = 0;

    static List<String> uniqList = new ArrayList<>();

    public static void main( String[] args ) throws IOException, InterruptedException {
        if(args.length != 1) {
            System.out.println("【使い方】 work.App 検索したいワード");
            return;
        }

        Deque<TreeNode> que = new ArrayDeque<>();

        List<String> outputList = new ArrayList<>();
        outputList.add(args[0]);

        Document wiki = Jsoup.connect("https://ja.wikipedia.org/wiki/" + args[0]).get();
        Elements contents = wiki.select(".mw-parser-output p").first().select(" a[href^=/wiki]");

        TreeNode node = new TreeNode(args[0], "", contents);
        que.add(node);
        setContents(que);

        que.clear();
        que.add(node);
        System.out.println("-" + node.value);
        displayNode("    ", que);
    }

    // 幅優先探索
    private static void setContents(Deque<TreeNode> que) throws IOException, InterruptedException {
        while(currentCnt <= MAX_CNT) {
            TreeNode node = que.poll();

            for(Element content : node.contents) {
                String hrefKeyword = URLDecoder.decode(content.attr("href"), "UTF-8").split("/")[2];

                String wordFlg = "";
                Elements contents = new Elements();
                if(isNgWord(hrefKeyword)) {
                    wordFlg = "$";
                } else if(isDuplicate(hrefKeyword)) {
                    wordFlg = "@";
                } else {
                    wordFlg = "$";
                    Thread.sleep(1000);
                    Document wiki = Jsoup.connect("https://ja.wikipedia.org/wiki/" + hrefKeyword).get();
                    
                    try {
                        contents = wiki.select(".mw-parser-output p").first().select(" a[href^=/wiki]");    
                    } catch (NullPointerException e) {
                        continue;
                    }
                    
    
                    uniqList.add(hrefKeyword);
                    currentCnt++;
                }

                TreeNode childNode = new TreeNode(hrefKeyword, wordFlg, contents);
                node.childNodes.add(childNode);
                que.add(childNode);

                if(currentCnt >= MAX_CNT) {
                    return;
                }

            }
        }

    }

    // 深さ優先探索
    private static void displayNode(String indent, Deque<TreeNode> que) throws IOException {
        while(!que.isEmpty()) {
            TreeNode node = que.pop();
            
            for(TreeNode childe: node.childNodes) {
                
                if(childe.childNodes.size() != 0) {
                    System.out.println(indent + "-" + childe.value);
                    que.add(childe);
                    displayNode(indent + "    ", que);
                } else {
                    System.out.println(indent + "-" + childe.value + childe.wordFlg);
                }
            }
        }
    }

    private static boolean isNgWord(String word) {
        Pattern ngWord1 = Pattern.compile("語$");
        Pattern ngWord2 = Pattern.compile("学$");
        return ngWord1.matcher(word).find() || ngWord2.matcher(word).find();
    }

    private static boolean isDuplicate(String word) {
        return uniqList.contains(word);
    }

}

class TreeNode {
    String value;
    String wordFlg;
    Elements contents;
    List<TreeNode> childNodes;

    TreeNode(String value, String wordString, Elements contents) {
        this.value = value;
        this.wordFlg = wordString;
        this.contents = contents;
        childNodes = new ArrayList<>();
    }

}