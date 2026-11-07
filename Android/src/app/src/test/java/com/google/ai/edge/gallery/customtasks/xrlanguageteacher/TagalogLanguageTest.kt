package com.google.ai.edge.gallery.customtasks.xrlanguageteacher

import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.ALL_SUPPORTED_LANGUAGES
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.TeacherLanguageOptions
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.localeTagToLanguage
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.mapEngineLocalesToLanguages
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.resolveEngineLocaleTagForLanguage
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.teacherResponseExamplePhrase
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.toEnglishName
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.models.toIsoLanguageCode
import com.google.ai.edge.gallery.customtasks.xrlanguageteacher.spokenphrases.XrLanguageTeacherSpokenPhrases
import com.google.ai.edge.gallery.proto.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TagalogLanguageTest {

  @Test
  fun tagalogIsInTheLanguageList() {
    assertTrue(Language.LANGUAGE_TAGALOG in ALL_SUPPORTED_LANGUAGES)
  }

  @Test
  fun engineTagsMapToTagalog() {
    assertEquals(Language.LANGUAGE_TAGALOG, localeTagToLanguage("fil-PH"))
    assertEquals(Language.LANGUAGE_TAGALOG, localeTagToLanguage("fil"))
    assertEquals(Language.LANGUAGE_TAGALOG, localeTagToLanguage("tl-PH"))
    assertEquals(Language.LANGUAGE_TAGALOG, localeTagToLanguage("tl"))
  }

  @Test
  fun systemVoiceWithFilipinoOffersTagalogToLearn() {
    val options =
      TeacherLanguageOptions.fromLocaleTags(
        asrLocaleTags = listOf("en-US"),
        ttsLocaleTags = listOf("en-US", "fil-PH", "es-ES"),
      )
    assertTrue(Language.LANGUAGE_TAGALOG in options.learnOptionsFor(Language.LANGUAGE_ENGLISH))
    assertEquals(
      listOf(Language.LANGUAGE_ENGLISH, Language.LANGUAGE_SPANISH, Language.LANGUAGE_TAGALOG),
      mapEngineLocalesToLanguages(listOf("es-ES", "fil-PH", "en-US")),
    )
  }

  @Test
  fun resolvesTheEngineTagForTagalog() {
    assertEquals(
      "fil-PH",
      resolveEngineLocaleTagForLanguage(Language.LANGUAGE_TAGALOG, listOf("en-US", "fil-PH")),
    )
    assertEquals(
      "tl-PH",
      resolveEngineLocaleTagForLanguage(Language.LANGUAGE_TAGALOG, listOf("en-US", "tl-PH")),
    )
  }

  @Test
  fun promptNameAndPhrases() {
    assertEquals("Tagalog (Filipino)", Language.LANGUAGE_TAGALOG.toEnglishName())
    assertEquals("fil", Language.LANGUAGE_TAGALOG.toIsoLanguageCode())
    assertEquals("ang tasa ng kape", teacherResponseExamplePhrase(Language.LANGUAGE_TAGALOG))
    val phrases = XrLanguageTeacherSpokenPhrases.forLanguageCode("tl-PH")
    assertTrue(phrases.greeting.startsWith("Kumusta"))
    assertEquals(phrases, XrLanguageTeacherSpokenPhrases.forLanguageCode("fil"))
  }
}
