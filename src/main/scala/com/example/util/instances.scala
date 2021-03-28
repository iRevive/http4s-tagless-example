package com.example.util

import com.example.util.json.JsonCodecs
import com.example.util.logging.RenderInstances

object instances {

  object all    extends JsonCodecs with RenderInstances
  object circe  extends JsonCodecs
  object render extends RenderInstances

}
