package net.slipp.support.test

import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(classOf[MockitoJUnitRunner])
abstract class MockitoIntegrationTest extends Fixture

abstract class UnitTest extends Fixture
