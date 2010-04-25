/*
 *  Model.scala
 *  (ScalaCollider)
 *
 *  Copyright (c) 2008-2010 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either
 *  version 2, june 1991 of the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License (gpl.txt) along with this software; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 *
 *
 *  Changelog:
 */

package de.sciss.synth

import scala.collection.immutable.{ Queue }

trait Model {
   private var listeners   = Queue.empty[ AnyRef => Unit ]
   private val sync        = new AnyRef

   protected def dispatch( change: AnyRef ) {
      listeners.foreach( l => try {
         l.apply( change )
      } catch {
         case e: MatchError => // ignored
         case e => e.printStackTrace() // catch, but print
      })
   }

   def addListener( l: AnyRef => Unit ) {
      sync.synchronized {
         listeners = listeners.enqueue( l )
      }
   }

   def removeListener( l: AnyRef => Unit ) {
      var filtered: Queue[ AnyRef => Unit ] = Queue.empty
      sync.synchronized {
         listeners = listeners.filterNot( _ == l )
      }
   }
}