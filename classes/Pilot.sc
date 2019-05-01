Pilot {
  *initClass {
    Class.initClassTree(Server);

    StartUp.add {
      Pilot.createEventType;
    };
  }

  *createEventType {
    Event.parentEvents.default[\pilotServer] = NetAddr("127.0.0.1", 49161);
    Event.addEventType(\pilot, { |server|
      var schdMsg, srvLatency, pilotServer, pilotMessage = "", msgGlue = "";
      var toABC = { |n| (n.notNil).if { "CcDdEFfGgAaB".asList[n.wrap(0,11)] } };
      var toH = { |value| ("0123456789abcdef".asList[value.asInt.wrap(0,15)] ?? 0).asString; };
      var to16 = { |value| ("0123456789abcdef".asList[(15 * value).round.asInt] ?? "f").asString; };
      var fx = { |f, wet=0, depth=0| "%%%".format(f, to16.(wet), to16.(depth)) };
      var fxList = { |... fxL| "%;".format(fxL.reject{ |f| f.isNil }.join(";")) };
      var setNote = { |chan, oct, note|
        if (note.isKindOf(Collection))
        { note.collect{ |n| setNote.(chan, oct, n) }.join(";"); }
        { "%%%".format(chan, oct.asInt, toABC.(note.asInt)); };
      };
      var bit, dis, aut, cho, tre, vib, rev, fee;

      srvLatency = server.latency ? 0.2;
      pilotServer = ~pilotServer.value;
      schdMsg = (thisThread.clock.tempo.reciprocal*~timingOffset.value)+~lag.value+srvLatency;
      if (~note.value != false) {
        pilotMessage = "%%%".format(
          setNote.(toH.(~chan.value ?? 0), ~octave.value ?? 4, ~note.value),
          to16.(~amp.value ?? 0.5),
          to16.(~sustain.value ?? 1/2),
        );
        msgGlue = ";";
      }
      ;
      bit = (~bitWet.value.notNil).if { fx.("BIT", ~bitWet.value, ~bitDepth.value ?? 0) }
      ;
      dis = (~disWet.value.notNil).if { fx.("DIS", ~disWet.value, ~disDepth.value ?? 0) }
      ;
      aut = (~autWet.value.notNil).if { fx.("AUT", ~autWet.value, ~autDepth.value ?? 0) }
      ;
      cho = (~choWet.value.notNil).if { fx.("CHO", ~choWet.value, ~choDepth.value ?? 0) }
      ;
      tre = (~treWet.value.notNil).if { fx.("TRE", ~treWet.value, ~treDepth.value ?? 0) }
      ;
      vib = (~vibWet.value.notNil).if { fx.("VIB", ~vibWet.value, ~vibDepth.value ?? 0) }
      ;
      rev = (~revWet.value.notNil).if { fx.("REV", ~revWet.value, ~revDepth.value ?? 0) }
      ;
      fee = (~feeWet.value.notNil).if { fx.("FEE", ~feeWet.value, ~feeDepth.value ?? 0) }
      ;
      SystemClock.sched(schdMsg, {
        pilotMessage = "%%%".format(
          pilotMessage, msgGlue,
          fxList.(bit, dis, aut, cho, tre, vib, rev, fee);
        );
        pilotServer.sendMsg(pilotMessage);
      });
    })
    ;
  }

}