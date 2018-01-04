/* 
 * Copyright 2005 Paul Hinds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tp23.antinstaller.runtime;

import java.util.StringTokenizer;

/**
 * Version helper accepts version numbers of the following format.
 * [major][clause].[minor][clause].[minor][clause].... ad infinitum
 * 
 * If the Java flag is set to true an attempt is made to parse the string as if it were
 * returned by System.getProperty("java.version");
 * Since pre 1.3.1 the system is not parsable the default is to accept the string
 * if there is a format error.
 * 
 * For the non java syntax getting the string wrong will result in failed tests
 * @author teknopaul
 *
 */
public class VersionHelper {
    
    public static final String CLAUSE_ALPHA = "alpha";
    public static final String CLAUSE_BETA = "beta";
    public static final String CLAUSE_GAMMA = "gamma";
    public static final String CLAUSE_JAVA_BETA = "ea";

    public boolean equalOrHigher(final String test, final String version) {
        return equalOrHigher(test, version, false);
    }

    public boolean majorVersionCompatible(final String test, final String version) {
        return getMajorVersion(test) == getMajorVersion(version);
    }

    /**
     * 
     * @param test java version string being tested
     * @param version java version string being used as reference
     * @param javaSyntax
     * @return true if the value of <code>test</code> is greater than or equal to the value of <code>version</code>
     */
    public boolean equalOrHigher(final String test, final String version, final boolean javaSyntax) {
        try {
            StringTokenizer testSt = new StringTokenizer(test, ".");
            StringTokenizer verSt = new StringTokenizer(version, ".");
            
            while (true) {
                boolean testMore = testSt.hasMoreTokens(); 
                boolean verMore = verSt.hasMoreTokens();
                if( ! testMore || ! verMore ){
                    break;
                }
                String testToken = testSt.nextToken();
                String verToken = verSt.nextToken();
                short testVer = getVersion(testToken);
                short versionVer = getVersion(verToken);
                if( testVer == versionVer ) {
                    if ( equalClause(getClause( testToken ), getClause(verToken)) ) {
                        continue;
                    }
                    else {
                        return higherClause(getClause( testToken ), getClause( verToken ), javaSyntax);
                    }
                }
                return testVer > versionVer ;
            }
            // equal up to one not having minor details
            if( countDots(test) >= countDots(version) ){
                return true;
            }
            return test.equals(version);
        } catch (Exception e) {
            // syntax exceptions
            if(javaSyntax){
                return true; // return true for Java since pre 1.3.1 or other JVMs could get any old rubbish
            }
            return false;
        }
    }
    
    public boolean isValid(final String version) {
        try {
            StringTokenizer verSt = new StringTokenizer(version, ".");
            
            boolean verMore = false;
            int i = 0;
            for (; verMore = verSt.hasMoreTokens(); i++) {
                
                String verToken = verSt.nextToken();
                if("".equals(verToken)){
                    return false;
                }
                // may throw NumberFormatExceptions
                getVersion(verToken);
                String clause = getClause(verToken);
                if( ! "".equals(clause)){
                    short clauseS = clauseToShort(clause);
                    if( ! (clauseS == 1 || clauseS == 2 || clauseS == 3 )){
                        return false;
                    }
                }         
            }
            if( ! verMore && i == 0) {
                return false; // nothing there!
            }
            return true;

        } 
        catch (Exception e) {
            // syntax exceptions
            return false;
        }
    }
    /**
     * @return the number part of the clause
     */
    private short getVersion(final String section) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < section.length(); i++) {
            char c = section.charAt(i);
            if(Character.isDigit(c)) {
                sb.append(c);
            }
            else{
                return Short.parseShort( sb.toString() );
            }
        }
        if(sb.length() > 0) {
            return Short.parseShort( sb.toString() );
        }
        return 0;
    }
    
    /**
     * @return  the clause eg beta or ""
     */
    private String getClause(final String section) {
        for (int i = 0; i < section.length(); i++) {
            char c = section.charAt(i);
            if(Character.isDigit(c)) {
                continue;
            }
            else {
                return section.substring(i);
            }
        }
        return "";
    }

    private boolean higherClause(final String test, final String clause, final boolean javaSyntax) {
        if(javaSyntax) {
            return clauseJavaToShort(test) > clauseJavaToShort(clause);
        }
        else {
            return clauseToShort(test) > clauseToShort(clause);
        }
            
    }
    private boolean equalClause(final String test, final String clause) {
        return clauseToShort(test) == clauseToShort(clause);
    }
    
    private short clauseToShort(String clause) {
        if(clause.startsWith("-")){
            clause = clause.substring(1); // knock off java style 1-beta dashes
        }
        if( CLAUSE_ALPHA.equals(clause) ) {
            return 3;
        }
        else if( CLAUSE_BETA.equals(clause) ) {
            return 2;
        }
        else if( CLAUSE_GAMMA.equals(clause) ) {
            return 1;
        }
        if(clause.startsWith("_")) { // java build version support 1_03-ea-beta  (discarding the sub sub clause "-ea-beta" because I'm lazy)
            int hasDash = clause.indexOf('-');
            if(hasDash > -1) {
                return Short.parseShort(clause.substring(1, hasDash));
            }
            else {
                return Short.parseShort(clause.substring(1));
            }
        }
        else return Short.MAX_VALUE; // no clause assumes higher
    }
    
    private short clauseJavaToShort(String clause) {
        if(clause.startsWith("-")){
            clause = clause.substring(1); // knock off java style 1-beta dashes
        }
        else if( CLAUSE_JAVA_BETA.equals(clause) ) {  // -ea early access are assumed to be less good
            return -2;
        }
        if(clause.startsWith("_")) { // java build version support 1_03-ea-beta  (discarding the sub sub clause "-ea-beta" because I'm lazy)
            int hasDash = clause.indexOf('-');
            if(hasDash > -1) {
                return Short.parseShort(clause.substring(1, hasDash));
            }
            else {
                return Short.parseShort(clause.substring(1));
            }
        }
        else return 0; // no clause assumes lower in Java speak
    }

    private short countDots(final String fullver){
        short count = 0;
        for (int i = 0; i < fullver.length(); i++) {
            if(fullver.charAt(i) == '.') {
                count++;
            }
        }
        return count;
    }
    
    private short getMajorVersion(String test){
        return getVersion(test);
    }

}
