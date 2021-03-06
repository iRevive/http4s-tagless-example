package com.example.util

import com.example.util.config.ToConfigOps
import com.example.util.error.ToErrorRaiseOps
import com.example.util.json.ToJsonOps

object syntax {

  object all    extends ToConfigOps with ToJsonOps with mtl.ToAllMtlOps
  object config extends ToConfigOps
  object json   extends ToJsonOps

  object mtl {
    private[syntax] trait ToAllMtlOps extends ToErrorRaiseOps

    object all   extends ToAllMtlOps
    object raise extends ToErrorRaiseOps
  }

}
