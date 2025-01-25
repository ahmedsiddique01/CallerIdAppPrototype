package com.caller.id.app.prototype.usecases

import com.caller.id.app.prototype.CoroutineTestRule
import com.caller.id.app.prototype.data.repository.contacts.ContactsRepository
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.domain.usecases.contacts.ContactsUseCase
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ContactsUseCaseTest {

    private lateinit var contactsUseCase: ContactsUseCase
    private val contactsRepository = mockk<ContactsRepository>()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        contactsUseCase = ContactsUseCase(contactsRepository)
    }

    @Test
    fun `fetch contacts successfully`() = runTest {
        val contact = Contact(
            id = "1",
            name = "John Doe",
            number = "123456789",
        )
        every { contactsRepository.getContacts() } returns flow {
            emit(listOf(contact))
        }

        val result = contactsUseCase.getContacts().first()
        result.size shouldBe 1
        result.first().name shouldBe "John Doe"
    }

    @Test
    fun `fetch contacts with empty list`() = runTest {
        every { contactsRepository.getContacts() } returns flow {
            emit(emptyList())
        }

        val result = contactsUseCase.getContacts().first()
        result.size shouldBe 0
    }

    @Test
    fun `search contacts successfully`() = runTest {
        val contact = Contact(
            id = "1",
            name = "Jane Doe",
            number = "987654321",
        )
        every { contactsRepository.searchContacts("Jane") } returns flow {
            emit(listOf(contact))
        }

        val result = contactsUseCase.searchContacts("Jane").first()
        result.size shouldBe 1
        result.first().name shouldBe "Jane Doe"
    }

    @Test
    fun `search contacts with no results`() = runTest {
        every { contactsRepository.searchContacts("Unknown") } returns flow {
            emit(emptyList())
        }

        val result = contactsUseCase.searchContacts("Unknown").first()
        result.size shouldBe 0
    }
}
