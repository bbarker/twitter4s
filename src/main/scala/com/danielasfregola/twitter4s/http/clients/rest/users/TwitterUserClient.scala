package com.danielasfregola.twitter4s.http.clients.rest.users

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{Banners, User}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.rest.users.parameters.{UserSearchParameters, BannersParameters, UserParameters, UsersParameters}
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `users` resource.
  */
trait TwitterUserClient extends OAuthClient with Configurations {

  private val usersUrl = s"$apiTwitterUrl/$twitterVersion/users"

  /** Returns fully-hydrated user objects for up to 100 users per request, as specified by the sequence of screen name parameters.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/lookup</a>.
    *
    * @param screen_names : A sequence of screen names, up to 100 are allowed in a single request.
    * @return : The sequence of user representations.
    */
  def getUsers(screen_names: String*): Future[Seq[User]] = getUsers(screen_names)

  /** Returns fully-hydrated user objects for up to 100 users per request, as specified by the sequence of screen name parameters.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/lookup</a>.
    *
    * @param screen_names : A sequence of screen names, up to 100 are allowed in a single request.
    * @param include_entities : By default it is `true`.
    *                         The parameters node that may appear within embedded statuses will be disincluded when set to `false`.
    * @return : The sequence of user representations.
    */
  def getUsers(screen_names: Seq[String],
               include_entities: Boolean = true): Future[Seq[User]] = {
    require(!screen_names.isEmpty, "please, provide at least one screen name")
    val parameters = UsersParameters(user_id = None, Some(screen_names.mkString(",")), include_entities)
    genericGetUsers(parameters)
  }

  /** Returns fully-hydrated user objects for up to 100 users per request, as specified by the sequence of user id parameters.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/lookup</a>.
    *
    * @param ids : A sequence of user ids, up to 100 are allowed in a single request.
    * @return : The sequence of user representations.
    */
  def getUsersByIds(ids: Long*): Future[Seq[User]] = getUsersByIds(ids)

  /** Returns fully-hydrated user objects for up to 100 users per request, as specified by the sequence of user id parameters.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/lookup" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/lookup</a>.
    *
    * @param ids : A sequence of user ids, up to 100 are allowed in a single request.
    * @param include_entities : By default it is `true`.
    *                         The parameters node that may appear within embedded statuses will be disincluded when set to `false`.
    * @return : The sequence of user representations.
    */
  def getUsersByIds(ids: Seq[Long],
                    include_entities: Boolean = true): Future[Seq[User]] = {
    require(!ids.isEmpty, "please, provide at least one user id")
    val parameters = UsersParameters(Some(ids.mkString(",")), screen_name = None, include_entities)
    genericGetUsers(parameters)
  }

  private def genericGetUsers(parameters: UsersParameters): Future[Seq[User]] =
    Get(s"$usersUrl/lookup.json", parameters).respondAs[Seq[User]]

  /** Returns a variety of information about the user specified by the required screen name parameter.
    * The author’s most recent Tweet will be returned inline when possible.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/show</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results for.
    * @param include_entities : By default it is `true`.
    *                         The parameters node that may appear within embedded statuses will be disincluded when set to `false`.
    * @return : The sequence of user representations.
    */
  def getUser(screen_name: String, include_entities: Boolean = true): Future[User] = {
    val parameters = UserParameters(user_id = None, Some(screen_name), include_entities)
    genericGetUser(parameters)
  }

  /** Returns a variety of information about the user specified by the required screen name parameter.
    * The author’s most recent Tweet will be returned inline when possible.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/show" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/show</a>.
    *
    * @param id : The ID of the user for whom to return results for.
    * @param include_entities : By default it is `true`.
    *                         The parameters node that may appear within embedded statuses will be disincluded when set to `false`.
    * @return : The sequence of user representations.
    */
  def getUserById(id: Long, include_entities: Boolean = true): Future[User] = {
    val parameters = UserParameters(Some(id), screen_name = None, include_entities)
    genericGetUser(parameters)
  }

  private def genericGetUser(parameters: UserParameters): Future[User] =
    Get(s"$usersUrl/show.json", parameters).respondAs[User]

  /** Returns a map of the available size variations of the specified user’s profile banner.
    * If the user has not uploaded a profile banner, a `TwitterException` will be thrown instead.
    * This method can be used instead of string manipulation on the profile_banner_url returned in user objects as described in Profile Images and Banners.
    * The profile banner data available at each size variant’s URL is in PNG format.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/profile_banner" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/profile_banner</a>.
    *
    * @param screen_name : The screen name of the user for whom to return results.
    *                    Helpful for disambiguating when a valid screen name is also a user ID.
    * @return : The banners representation.
    */
  def getProfileBannersForUser(screen_name: String): Future[Banners] = {
    val parameters = BannersParameters(user_id = None, Some(screen_name))
    genericGetProfileBanners(parameters)
  }

  /** Returns a map of the available size variations of the specified user’s profile banner.
    * If the user has not uploaded a profile banner, a `TwitterException` will be thrown instead.
    * This method can be used instead of string manipulation on the profile_banner_url returned in user objects as described in Profile Images and Banners.
    * The profile banner data available at each size variant’s URL is in PNG format.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/profile_banner" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/profile_banner</a>.
    *
    * @param user_id : The ID of the user for whom to return results.
    *                Helpful for disambiguating when a valid user ID is also a valid screen name.
    * @return : The banners representation.
    */
  def getProfileBannersForUserId(user_id: Long): Future[Banners] = {
    val parameters = BannersParameters(Some(user_id), screen_name = None)
    genericGetProfileBanners(parameters)
  }

  private def genericGetProfileBanners(parameters: BannersParameters): Future[Banners] =
    Get(s"$usersUrl/profile_banner.json", parameters).respondAs[Banners]

  /** Provides a simple, relevance-based search interface to public user accounts on Twitter.
    * Try querying by topical interest, full name, company name, location, or other criteria.
    * Exact match searches are not supported. Only the first 1,000 matching results are available.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/users/search" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/users/search</a>.
    *
    * @param query : The search query to run against people search.
    * @param page : By default it is `-1`, which is the first "page".
    *             Specifies the page of results to retrieve.
    * @param count : By default it is `20`.
    *              The number of potential user results to retrieve per page.
    *              This value has a maximum of 20.
    * @param include_entities : By default it is `true`.
    *                         The parameters node that may appear within embedded statuses will be disincluded when set to `false`.
    * @return : The sequence of users.
    */
  def searchForUser(query: String,
                    page: Int = -1,
                    count: Int = 20,
                    include_entities: Boolean = true): Future[Seq[User]] = {
    val parameters = UserSearchParameters(query, page, count, include_entities)
    Get(s"$usersUrl/search.json", parameters).respondAs[Seq[User]]
  }
}
