package org.chening.text.core;

import org.chening.text.jieba.JiebaTools;
import org.chening.text.pso.PSODriver;
import org.chening.text.semantic.SemanticUilt;

import java.io.File;

/**
 * Created by linznin on 2017/3/25.
 */
public class main {

    public static void main(String[] args) {
        new main().run(args);
    }

    private void run(String[] args){
        parse_command_line(args);
        checkSystemPath();
        if (ProSetting.OPTIMIZATION_FUNCTION.equals(Constants.SEMANTIC)) {
            new SemanticUilt().execute();
        } else if (ProSetting.OPTIMIZATION_FUNCTION.equals(Constants.JIEBA)){
            new JiebaTools().jiebaSeq();
        }
        else {
            new PSODriver().run();
        }
    }

    private void checkSystemPath() {
        File file = new File(ProSetting.ORG_PATH);
        if (!file.exists()){
            System.err.print("Wrong system path!\n");
            System.exit(1);
        }
        if (ProSetting.OPTIMIZATION_FUNCTION.equals(Constants.JIEBA) && !file.isFile()){
            System.err.print("This is not a file!\n");
            System.exit(1);
        }
        ProSetting.genPath();
    }

    private void parse_command_line(String[] args){
        int i;
        for(i=0;i<args.length;i++) {
            if (args[i].charAt(0) != '-') break;
            if (++i >= args.length)
                exit_with_help();
            switch (args[i - 1].charAt(1)) {
                case 'f':
                    ProSetting.OPTIMIZATION_FUNCTION = checkFunction(args[i]);
                    break;
                case 'h':
                    exit_with_help();
                    break;
                default:
                    System.err.print("Unknown option: " + args[i - 1] + "\n");
                    exit_with_help();
            }
        }
        if (args.length>i)
            ProSetting.ORG_PATH = args[i];
    }

    private String checkFunction(String arg) {
        if (arg.equals(Constants.OPTIMIZATION_LDA) ||
                arg.equals(Constants.OPTIMIZATION_LDA_SEMANTIC) ||
                arg.equals(Constants.OPTIMIZATION_SEMANTIC) ||
                arg.equals(Constants.SEMANTIC) ||
                arg.equals(Constants.JIEBA))
            return arg;
        else
            exit_with_help();
        return "";
    }

    private static void exit_with_help()
    {
        System.out.print(
                "Usage:  [options] [system path]\n"
                        +"options:\n"
                        +"-f function_type : set type of function(default 0)\n"
                        +"	0 -- LDA & Semantic optimization\n"
                        +"	1 -- LDA optimization\n"
                        +"	2 -- Semantic optimization\n"
                        +"	3 -- Semantic analytics \n"
                        +"	4 -- jieba tools , give [system path] as file\n"
        );
        System.exit(1);
    }
}
