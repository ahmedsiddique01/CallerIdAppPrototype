package com.caller.id.app.prototype.viewmodels

import com.caller.id.app.prototype.CoroutineTestRule
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.domain.usecases.contacts.ContactsUseCase
import com.caller.id.app.prototype.presentation.contacts.viewmodel.ContactsViewModel
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ContactsViewModelTest {

    private lateinit var viewModel: ContactsViewModel
    private val contactsUseCase = mockk<ContactsUseCase>()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        viewModel = ContactsViewModel(contactsUseCase)
    }

    @Test
    fun `fetch contacts successfully`() = runTest {
        val contact = Contact(
            id = "1",
            name = "John Doe",
            number = "123456789",
        )
        // Properly stubbing the mock response
        every { contactsUseCase.getContacts() } returns flow {
            emit(listOf(contact, contact))
        }

        viewModel.fetchContacts()
        advanceUntilIdle()

        val result = viewModel.contacts.value
        result?.size shouldBe 2
    }

    @Test
    fun `fetch contacts with empty list`() = runTest {
        // Properly stubbing the mock response

        every { contactsUseCase.getContacts() } returns flow {
            emit(emptyList())
        }

        viewModel.fetchContacts()
        advanceUntilIdle()

        viewModel.contacts.value?.let {
            it.size shouldBe 0
        }
    }

    @Test
    fun `search contacts successfully`() = runTest {
        val contact = Contact(
            id = "1",
            name = "Jane Doe",
            number = "987654321",
        )
        // Properly stubbing the search call
        every { contactsUseCase.searchContacts("Jane") } returns flow {
            emit(listOf(contact))
        }

        viewModel.searchContacts("Jane")
        advanceUntilIdle()

        val result = viewModel.contacts.value
        result?.size shouldBe 1
        result?.first()?.name shouldBe "Jane Doe"
    }

    @Test
    fun `search contacts with no results`() = runTest {
        // Properly stubbing the search call
        every { contactsUseCase.searchContacts("Unknown") } returns flow {
            emit(emptyList())
        }

        viewModel.searchContacts("Unknown")
        advanceUntilIdle()

        val result = viewModel.contacts.value
        result?.size shouldBe 0
    }
}
