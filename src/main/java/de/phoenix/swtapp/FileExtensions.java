/*
 * Copyright (C) 2014 Project-Phoenix
 * 
 * This file is part of Tool.
 * 
 * Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Tool.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.swtapp;

import java.util.HashSet;
import java.util.Set;

// This class is needed to decide whether the downloaded data is a pattern or an attachement
public class FileExtensions {
    private String[] fileExtension = {".as", ".ABAP", ".ABC", ".as", ".adb", ".ads", ".agda", ".lagda", ".prj", ".aim", ".amb", ".aimmspack", ".dat", ".a68", ".ap", ".at", ".e", ".mod", ".dat", ".run", ".scpt", ".AppleScript", ".arc", ".aj", ".asm", ".au3", ".b", ".bat", ".cmd", ".btm", ".c", ".h", ".cc", ".cpp", ".cxx", ".c++", ".h", ".hh", ".hpp", ".hxx", ".h++", ".cs", ".ceylon", ".cfm", ".cfc", ".chpl", ".cl", ".icl", ".dcl", ".abc", ".obj", ".clj", ".edn", ".cobra", ".coffee", ".lisp", ".v", ".d", ".dart", ".p", ".pp", ".pas", ".d", ".g", ".ex", ".exs", ".elm", ".erl", ".hrl", ".e", ".ex", ".exw", ".edb", ".fs", ".ftd", ".fal", ".fam", ".fy", ".fyc", ".fancypack", ".fjo", ".fjv", ".sma", ".ein", ".f", ".for", ".f90", ".f95", ".mpt", ".mpf", ".nc", ".gs", ".go", ".gsp", ".gs", ".gst", ".gsx", ".prg", ".ch", ".hb", ".hbp", ".hs", ".lhs", ".hx", ".hxml", ".idr", ".lidr", ".ik", ".java", ".class", ".jar", ".js", ".jse", ".wsf", ".wsc", ".html", ".asp", ".jl", ".kojo", ".scala", ".kt", ".lasso", ".LassoApp", ".ls", ".m", ".m", ".mv", ".mvc", ".mvt", ".mod", ".m2", ".def", ".MOD", ".DEF", ".mi", ".md", ".mrc", ".ini", ".neko", ".n", ".nlogo", ".nlogo3d", ".nrx", ".osc", ".rxs", ".rex", ".p", ".pp", ".pas", ".h", ".m", ".mm", ".pl", ".pm", ".t", ".pod", ".php", ".phtml", ".phps", ".php3", ".php4", ".php5", ".pde", ".pl", ".pro", ".P", ".pvx", ".pvc", ".pvk", ".pvt", ".py", ".pyw", ".pyc", ".pyo", ".pyd", ".rkt", ".rktl", ".rktd", ".scrbl", ".plt", ".ss", ".scm", ".r", ".reb", ".red", ".exec", ".rexx", ".rex", ".rb", ".rbw", ".rs", ".scala", ".scm", ".ss", ".sb", ".sb2", ".sd7", ".s7i", ".bas", ".sml", ".nut", ".sv", ".tcl", ".ts", ".ump", ".uc", ".uci", ".upkg", ".vala", ".vapi", ".vbs", ".vbe", ".wsf", ".wsc", ".v", ".vb", ".x10", ".xc", ".xq", ".xqy", ".xquery", ".P", ".i"};
    Set<String> fileEndings = new HashSet<String>();

    public FileExtensions() {

        for (int j = 0; j < fileExtension.length; j++) {
            fileEndings.add(fileExtension[j]);
        }

    }

    public boolean isTextFile(String fileName) {
        String fileEnding = fileName;
        String subTemp = "";
        String[] pathComponents = null;

        pathComponents = fileEnding.split("\\\\");
        subTemp = (pathComponents[pathComponents.length - 1]);
        pathComponents = subTemp.split("\\.");

        fileEnding = "." + (pathComponents[pathComponents.length - 1]);

        if (fileEndings.contains(fileEnding)) {
            return true;
        } else {

            return false;
        }

    }
}
