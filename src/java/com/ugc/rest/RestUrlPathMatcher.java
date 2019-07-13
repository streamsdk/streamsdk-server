package com.ugc.rest;

import org.springframework.util.PathMatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestUrlPathMatcher implements PathMatcher
{
    private static final Pattern WILDCARD_PATTERN = Pattern.compile("([\\\\\\/])|(\\([^\\)]+\\))|(\\*{1,2})|(\\?)|(\\\\\\/)");
    private static final Pattern WILDCARD_FREE_PATTERN = Pattern.compile("^[\\\\\\/]?([^\\(\\*\\?\\\\\\/]*[\\\\\\/])*([^\\(\\*\\?\\\\\\/]*$)?");
    private static final Pattern REGEX_ESCAPE_PATTERN = Pattern.compile("[\\\\\\/\\[\\]\\^\\$\\.\\{\\}\\&\\?\\*\\+\\|\\<\\>\\!\\=]");

    private Map<String, NamedPattern> _patternCache = new HashMap<String, NamedPattern>();

    public boolean isPattern(String string) {
        return (string.indexOf('*') != -1 || string.indexOf('?') != -1 || string.indexOf('(') != -1);
    }

    public boolean match(String pattern, String path) {
        return getOrCreatePattern(pattern).matcher(path).matches();
    }

    public boolean matchStart(String pattern, String path) {
        return getOrCreatePattern(pattern).matcher(path).matches();
    }

    public String extractPathWithinPattern(String pattern, String path) {
        return getOrCreatePattern(pattern).extractPathWithinPattern(path);
    }

    public Map<String, String> namedParameters(String pattern, String path) {
        return getOrCreatePattern(pattern).namedGroups(path);
    }

    private NamedPattern getOrCreatePattern(String pattern) {
        NamedPattern compiledPattern = _patternCache.get(pattern);
        if (compiledPattern == null) {
            compiledPattern = new NamedPattern(pattern);
            _patternCache.put(pattern, compiledPattern);
        }

        return compiledPattern;
    }

    private class NamedPattern
    {
        private Pattern _pattern = null;
        private List<String> _names = null;
        private int _patternMatchedIndex = 0;

        public NamedPattern(String pattern) {
            Matcher patternFree = WILDCARD_FREE_PATTERN.matcher(pattern);
            if (patternFree.find())
                _patternMatchedIndex = patternFree.group(0).length();

            _names = new ArrayList<String>();
            String translated = translatePattern(pattern, new BitSet(1));
            _pattern = Pattern.compile('^' + translated + '$');
        }

        public Matcher matcher(String string) {
            return _pattern.matcher(string);
        }

        public Map<String, String> namedGroups(String path) {
            Matcher matcher = matcher(path);
            if (!matcher.matches())
                return null;

            Map<String, String> groups = new HashMap<String, String>();

            try {
                for (int i = 0; i < _names.size(); i++)
                    if ((matcher.group(i + 1) != null) && (_names.get(i) != null))
                        groups.put(_names.get(i), URLDecoder.decode(matcher.group(i + 1), "UTF-8"));
            }
            catch (UnsupportedEncodingException uee) {
                return null;
            }

            return groups;
        }

        public String extractPathWithinPattern(String path) {

            if (path.length() < _patternMatchedIndex)
                return "";

            return path.substring(_patternMatchedIndex);
        }

        private String translatePattern(String pattern, BitSet nextSeparatorOptional) {
            StringBuffer translatedPattern = new StringBuffer();
            Matcher m = WILDCARD_PATTERN.matcher(pattern);

            int lastFound = 0;
            while (m.find()) {
                String content = REGEX_ESCAPE_PATTERN.matcher(pattern.substring(lastFound, m.start())).replaceAll("\\\\$0");
                translatedPattern.append(content);

                if (content.length() > 0)
                    nextSeparatorOptional.set(0, false);

                if (m.group(1) != null) {

                    translatedPattern.append("[\\\\\\/]");

                    if (nextSeparatorOptional.get(0))
                        translatedPattern.append('?');

                    nextSeparatorOptional.set(0, true);

                } else if ("*".equals(m.group(3)))
                    translatedPattern.append("[^\\\\\\/]*");

                else if ("**".equals(m.group(3)))
                    translatedPattern.append(".*?");

                else if (m.group(4) != null) {

                    translatedPattern.append(".");
                    nextSeparatorOptional.set(0, false);

                } else if (m.group(2) != null) {

                    int colonIndex = m.group(2).indexOf(':');

                    if (colonIndex < 0)
                        throw new RestUrlPathMatcherException("Named group does not contain name '" + m.group(2) + "'");

                    _names.add(m.group(2).substring(colonIndex + 1, m.group(2).length() - 1));
                    translatedPattern.append("(").append(translatePattern(m.group(2).substring(1, colonIndex), nextSeparatorOptional)).append(")");
                }

                lastFound = m.end();
            }

            translatedPattern.append(REGEX_ESCAPE_PATTERN.matcher(pattern.substring(lastFound)).replaceAll("\\\\$0"));

            return translatedPattern.toString();
        }
    }
}
