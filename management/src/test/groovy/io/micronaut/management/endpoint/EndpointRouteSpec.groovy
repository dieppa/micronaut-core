/*
 * Copyright 2017-2018 original authors
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
package io.micronaut.management.endpoint

import io.micronaut.context.ApplicationContext
import io.micronaut.http.annotation.QueryValue
import io.micronaut.web.router.Router
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.annotation.Nullable
import java.security.Principal

import static io.micronaut.http.HttpMethod.*

class EndpointRouteSpec extends Specification {

    @Shared @AutoCleanup ApplicationContext ctx = ApplicationContext.run()

    @Unroll
    void "test #method to #uri #description"() {
        when:
        Router router = ctx.getBean(Router)

        then:
        router.find(method, uri).count() == (exists ? 1 : 0)

        where:
        method | uri      | exists
        GET    | '/a'     | true
        POST   | '/a'     | true
        DELETE | '/a'     | true
        GET    | '/b'     | true
        POST   | '/b'     | true
        DELETE | '/b'     | true
        GET    | '/c/foo' | true
        GET    | '/c'     | false
        POST   | '/c'     | false
        POST   | '/c/foo' | true
        DELETE | '/c/foo' | true
        DELETE | '/c'     | false
        GET    | '/d/foo' | true
        GET    | '/d'     | false
        POST   | '/d'     | true
        DELETE | '/d/foo' | true
        DELETE | '/d'     | false
        description = exists ? "exists" : "does not exist"
    }


    @Endpoint('a')
    static class AEndpoint {

        @Read
        String one(Principal principal) {}

        @Write
        String two(Principal principal) {}

        @Delete
        String three(Principal principal) {}
    }

    @Endpoint('b')
    static class BEndpoint {

        @Read
        String one(@Nullable String name) {}

        @Write
        String two(@Nullable String name) {}

        @Delete
        String three(@Nullable String name) {}
    }

    @Endpoint('c')
    static class CEndpoint {

        @Read
        String one(@QueryValue String name) {}

        @Write
        String two(@QueryValue String name) {}

        @Delete
        String three(@QueryValue String name) {}
    }

    @Endpoint('d')
    static class DEndpoint {

        @Read
        String one(String name) {}

        @Write
        String two(String name) {}

        @Delete
        String three(String name) {}
    }
}
