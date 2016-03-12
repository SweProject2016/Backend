package de.hwrberlin.it2014.sweproject.rest;

import de.hwrberlin.it2014.sweproject.model.Entity;
import de.hwrberlin.it2014.sweproject.model.Status;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

/**
 * @author Stefan Hentschel
 */
public class TestResource {

  private Resource resource;

  @Before
  public void before() {
    resource = new ImportResource();
  }

  @Test
  public void testWrongResourceAuthentication() {
    assertFalse(resource.auth("nope", "more nope"));
  }

  @Test
  public void testRightResourceAuthentication() {
    assertTrue(resource.auth("$A$9af4d8381781baccb0f915e554f8798d", "$T$de61425667e2e4ac0884808b769cd042"));
  }

  @Test
  public void testNullResourceAuthentication() {
    assertFalse(resource.auth(null, null));
  }

  @Test
  public void testBuildResponse() {
    Response build = resource.build(Response.Status.ACCEPTED);
    assertEquals(build.getStatus(), 202);
  }

  /**
   * Fails because we have a default equals
   */
  @Test
  public void testResponseBuildWithEntity() {
    Entity entity = new Entity("/", 200, new Object());
    Response build = resource.build(Response.Status.ACCEPTED, entity, "/");
    Entity ent = (Entity) build.getEntity();

    assertEquals(ent.getEntity(), entity);
  }

  @Test
  public void testResponseBuildWithNullEntity() {
    Response build = resource.build(Response.Status.ACCEPTED, null, "/");
    assertEquals(((Entity) build.getEntity()).getEntity(), null);
  }

  @Test
  public void testResponseBuildWithNullValues() {
    Response build = resource.build(Response.Status.ACCEPTED, null, null);
    assertEquals(((Entity) build.getEntity()).getEntity(), null);
  }

  @Test
  public void testResponseBuildWithNullStatus() {
    try {
      Response build = resource.build(null, null, null);
      fail("u shall not pass!");
    } catch (IllegalArgumentException iaex) {
      // ok
    }
  }
}
