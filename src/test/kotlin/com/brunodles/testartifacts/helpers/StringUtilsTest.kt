package com.brunodles.testartifacts.helpers

import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

import  com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import  com.brunodles.testartifacts.helpers.StringUtils.underscoreToCamelCase
import  org.junit.Assert.assertEquals

@RunWith(Enclosed::class)
class StringUtilsTest {

    class WhenRemoveEspecialCharacters {
        @Test
        fun withSimpleString_shouldReturnTheSameString() {
            assertEquals("123abc", removeEspecialCharacters("123abc"))
        }

        @Test
        fun withSpellingAccents_shouldReturnTextWithoutAccents() {
            assertEquals("aaaaaeeeeeiiiiiooooouuuuuyyyyynnncc", removeEspecialCharacters("áàâãäéèêẽëíìîĩïóòôõöúùûũüýỳŷỹÿñńǹçḉ"))
        }

        @Test
        fun withUrl_shouldRemoveSlashesAndDots() {
            assertEquals("httpsgithubcombrunodlesTestArtifacts", removeEspecialCharacters("https://github.com/brunodles/TestArtifacts"))
        }

        @Test
        fun withLinuxPath_shouldRemoveSlashes() {
            assertEquals("homeworkspaceanimewatchermagicfilejson", removeEspecialCharacters("~/home/workspace/anime-watcher/magicfile.json"))
        }

        @Test
        fun withWindowsPath_shouldRemoveSlashes() {
            assertEquals("cworkspaceanimewatchermagicfile2json", removeEspecialCharacters("c:/workspace/anime-watcher/magicfile2.json"))
        }

        @Test
        fun withPunctuation_shouldRemovePunctuation() {
            assertEquals("", removeEspecialCharacters("\"',.!@#\$%&*()[]{},.;<>:\\\\|/?´`~^¹²³£¢¬§ªº/?€®ŧ←↓→øþæßðđŋħł»©“”µ"))
        }

        @Test
        fun withMixedCase_shouldNotChangeIt() {
            assertEquals("aAbcdEe", removeEspecialCharacters("\\à,À.b;cdÊẽ~"))
        }

        @Test(expected = NullPointerException::class)
        fun withNullString_shouldThrowNullPointerException() {
            removeEspecialCharacters(null)
        }

        @Test
        fun withEmptyString_shouldReturnTheSameString() {
            assertEquals("", removeEspecialCharacters(""))
        }

        @Test
        fun withWhiteSpaces_shouldRemoveThen() {
            assertEquals("wow", removeEspecialCharacters("\tw\no w  \t \t\n\t "))
        }
    }

    class WhenUnderscoreToCamelCase {

        @Test
        fun withoutUnderscore_shouldReturnSameString() {
            assertEquals("Look.this-string", underscoreToCamelCase("Look.this-string"))
        }

        @Test
        fun withUnderscore_shouldParseToCamelCase() {
            assertEquals("manLookThis", underscoreToCamelCase("man_look_this"))
        }

        @Test
        fun withSpaces_shouldRemoveIt() {
            assertEquals("iHaveSpaces", underscoreToCamelCase("i have spaces"))
        }

        @Test
        fun withEmptyString_shouldReturnEmptyString() {
            assertEquals("", underscoreToCamelCase(""))
        }

        @Test
        fun withWhiteSpaces_shouldReturnEmptyString() {
            assertEquals("", underscoreToCamelCase(" \t \n"))
        }

        @Test(expected = NullPointerException::class)
        fun withNullString_shouldThrowNullPointerException() {
            removeEspecialCharacters(null)
        }
    }
}
