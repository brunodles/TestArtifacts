package com.brunodles.testartifacts.helpers

import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

import static com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import static com.brunodles.testartifacts.helpers.StringUtils.underscoreToCamelCase
import static org.junit.Assert.assertEquals

@RunWith(Enclosed.class)
class StringUtilsTest {

    static class WhenRemoveEspecialCharacters {
        @Test
        void withSimpleString_shouldReturnTheSameString() {
            assertEquals("123abc", removeEspecialCharacters("123abc"))
        }

        @Test
        void withSpellingAccents_shouldReturnTextWithoutAccents() {
            assertEquals("aaaaaeeeeeiiiiiooooouuuuuyyyyynnncc", removeEspecialCharacters("áàâãäéèêẽëíìîĩïóòôõöúùûũüýỳŷỹÿñńǹçḉ"))
        }

        @Test
        void withUrl_shouldRemoveSlashesAndDots() {
            assertEquals("httpsgithubcombrunodlesTestArtifacts", removeEspecialCharacters("https://github.com/brunodles/TestArtifacts"))
        }

        @Test
        void withLinuxPath_shouldRemoveSlashes() {
            assertEquals("homeworkspaceanimewatchermagicfilejson", removeEspecialCharacters("~/home/workspace/anime-watcher/magicfile.json"))
        }

        @Test
        void withWindowsPath_shouldRemoveSlashes() {
            assertEquals("cworkspaceanimewatchermagicfile2json", removeEspecialCharacters("c:/workspace/anime-watcher/magicfile2.json"))
        }

        @Test
        void withPunctuation_shouldRemovePunctuation() {
            assertEquals("", removeEspecialCharacters("\"',.!@#\$%&*()[]{},.;<>:\\\\|/?´`~^¹²³£¢¬§ªº/?€®ŧ←↓→øþæßðđŋħł»©“”µ"))
        }

        @Test
        void withMixedCase_shouldNotChangeIt() {
            assertEquals("aAbcdEe", removeEspecialCharacters("\\à,À.b;cdÊẽ~"))
        }

        @Test(expected = NullPointerException)
        void withNullString_shouldThrowNullPointerException() {
            removeEspecialCharacters(null)
        }

        @Test
        void withEmptyString_shouldReturnTheSameString() {
            assertEquals("", removeEspecialCharacters(""))
        }

        @Test
        void withWhiteSpaces_shouldRemoveThen() {
            assertEquals("wow", removeEspecialCharacters("\tw\no w  \t \t\n\t "))
        }
    }

    static class WhenUnderscoreToCamelCase {

        @Test
        void withoutUnderscore_shouldReturnSameString() {
            assertEquals("Look.this-string", underscoreToCamelCase("Look.this-string"))
        }

        @Test
        void withUnderscore_shouldParseToCamelCase() {
            assertEquals("manLookThis", underscoreToCamelCase("man_look_this"))
        }

        @Test
        void withSpaces_shouldRemoveIt() {
            assertEquals("iHaveSpaces", underscoreToCamelCase("i have spaces"))
        }

        @Test
        void withEmptyString_shouldReturnEmptyString() {
            assertEquals("", underscoreToCamelCase(""))
        }

        @Test
        void withWhiteSpaces_shouldReturnEmptyString() {
            assertEquals("", underscoreToCamelCase(" \t \n"))
        }

        @Test(expected = NullPointerException)
        void withNullString_shouldThrowNullPointerException() {
            removeEspecialCharacters(null)
        }
    }

}
