package com.danielasfregola.twitter4s.http.clients.media

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{MediaUpload, UploadedMedia}
import com.danielasfregola.twitter4s.http.clients.MediaOAuthClient
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterMediaClient extends MediaOAuthClient with Configurations {

  private val mediaUrl = s"$mediaTwitterUrl/$twitterVersion/media"

  private val BufferSize = 5 * 1024 * 1024


  // TODO - fix this!

  def uploadFromBytes(bytes: Array[Byte],
                      additional_owners: Seq[String] = Seq.empty): Future[UploadedMedia] = {
    val add_owners = if (!additional_owners.isEmpty) Some(additional_owners.mkString(",")) else None
    val mediaUpload = MediaUpload(bytes.mkString, add_owners)
    Post(s"$mediaUrl/upload.json", mediaUpload).respondAs[UploadedMedia]
  }
}