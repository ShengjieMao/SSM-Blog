import Vue from "vue";

(function (w, d) {
  function TheaterJS(options) {
    var self = this,
      defaults = { autoplay: true, erase: true };

    self.events = {};
    self.scene = -1;
    self.scenario = [];
    self.options = self.utils.merge(defaults, options || {});
    self.casting = {};
    self.current = {};
    self.state = "ready";
  }

  TheaterJS.prototype = {
    constructor: TheaterJS,

    set: function (value, args) {
      var self = this;

      self.current.model = value;

      switch (self.current.type) {
        case "function":
          self.current.voice.apply(self, args);
          break;

        default:
          self.current.voice.innerHTML = value;
          break;
      }

      return self;
    },

    getSayingSpeed: function (filter, constant) {
      if (typeof filter !== "number") {
        constant = filter;
        filter = 0;
      }

      var self = this,
        experience = self.current.experience + filter,
        skill = constant ? experience : self.utils.randomFloat(experience, 1);

      return self.utils.getPercentageBetween(1000, 50, skill);
    },

    getInvincibility: function () {
      var self = this;
      return self.current.experience * 10;
    },

    isMistaking: function () {
      var self = this;
      return self.current.experience < self.utils.randomFloat(0, 1.4);
    },

    utils: {
      merge: function (dest, origin) {
        for (var key in origin)
          if (origin.hasOwnProperty(key)) dest[key] = origin[key];
        return dest;
      },

      getPercentageBetween: function (min, max, perc) {
        return min - min * perc + max * perc;
      },

      randomChar: function () {
        var utils = this,
          chars = "abcdefghijklmnopqrstuvwxyz";

        return chars.charAt(utils.randomNumber(0, chars.length - 1));
      },

      randomNumber: function (min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
      },

      randomFloat: function (min, max) {
        return Math.round((Math.random() * (max - min) + min) * 10) / 10;
      },

      hasClass: function (el, className) {
        if (el.classList) return el.classList.contains(className);
        else
          return new RegExp("(^| )" + className + "( |$)", "gi").test(
            el.className
          );
      },

      addClass: function (el, className) {
        if (el.classList) el.classList.add(className);
        else el.className += " " + className;
      },

      removeClass: function (el, className) {
        if (el.classList) el.classList.remove(className);
        else
          el.className = el.className.replace(
            new RegExp(
              "(^|\\b)" + className.split(" ").join("|") + "(\\b|$)",
              "gi"
            ),
            " "
          );
      },
    },

    train: function (actor) {
      var self = this,
        defaults = {
          experience: 0.6,
          voice: function (newValue, newChar, prevChar, str) {
            console.log(newValue);
          },
          type: "function",
          model: "",
        };

      return self.utils.merge(defaults, actor);
    },

    describe: function (name, experience, voice) {
      if (typeof name !== "string")
        throw "actor's name has wrong type: " + typeof name;

      var self = this,
        actor = { name: name };

      if (experience !== void 0) actor.experience = experience;

      if (voice !== void 0) {
        actor.type = typeof voice === "function" ? "function" : "DOM";

        if (actor.type === "DOM")
          actor.voice =
            typeof voice === "string" ? d.querySelector(voice) : voice;
        else actor.voice = voice;
      }

      self.casting[name] = self.train(actor);
      return self;
    },

    write: function () {
      var self = this,
        scenes = Array.prototype.splice.apply(arguments, [0]),
        scene;

      for (var i = 0, l = scenes.length; i < l; i++) {
        scene = scenes[i];

        if (typeof scene === "string") {
          var params = scene.split(":"),
            hasActor = params.length > 1,
            actor = hasActor ? params[0].trim() : null,
            speech = hasActor ? params[1] : params[0];

          if (hasActor) self.write({ name: "actor", args: [actor] });
          if (self.options.erase && hasActor) self.write({ name: "erase" });
          self.write({ name: "say", args: [speech, !hasActor] });
        } else if (typeof scene === "number") {
          if (scene < 0) self.write({ name: "erase", args: [scene] });
          else self.write({ name: "wait", args: [scene] });
        } else if (typeof scene === "function") {
          self.write({ name: "call", args: [scene] });
        } else if (scene instanceof Object) {
          self.scenario.push(scene);
        }
      }

      if (self.options.autoplay) self.play();
      return self;
    },

    play: function (restart) {
      var self = this;

      if (restart === true) self.scene = -1;

      if (self.state === "ready") self.next();

      return self;
    },

    on: function (events, fn) {
      var self = this;

      events = events.split(",");

      for (var i = 0, l = events.length, event; i < l; i++) {
        event = events[i] = events[i].trim();
        (self.events[event] || (self.events[event] = [])).push(fn);
      }

      return self;
    },

    emit: function (scope, event, args) {
      if (typeof scope !== "string") throw "emit: scope missing";

      if (typeof event !== "string") event = void 0;
      else if (event !== void 0 && args === void 0) args = event;

      var self = this,
        eventName = scope + (event ? ":" + event : "");

      self.trigger(eventName, args).trigger("*", [eventName].concat(args));

      return self;
    },

    trigger: function (eventName, args) {
      var self = this,
        events = self.events[eventName] || [];

      args instanceof Array || (args = [args]);
      for (var i = 0, l = events.length; i < l; i++)
        events[i].apply(self, [eventName].concat(args));

      return self;
    },

    call: function (fn, async) {
      var self = this;

      fn.apply(self);
      return !async ? self.next() : self;
    },

    next: function () {
      var self = this,
        prevScene = self.scenario[self.scene];

      if (prevScene)
        self.emit(
          prevScene.name,
          "end",
          [prevScene.name].concat(prevScene.args)
        );

      if (self.scene + 1 >= self.scenario.length) {
        self.state = "ready";
      } else {
        self.state = "playing";

        var nextScene = self.scenario[++self.scene];

        self.emit(
          nextScene.name,
          "start",
          [nextScene.name].concat(nextScene.args)
        );
        self[nextScene.name].apply(self, nextScene.args);
      }

      return self;
    },

    actor: function (actor) {
      var self = this;

      self.current = self.casting[actor];
      return self.next();
    },

    say: function (speech, append) {
      var self = this,
        mistaken = false,
        invincible = self.getInvincibility(),
        cursor,
        model;

      if (append) {
        model = self.current.model;
        cursor = self.current.model.length - 1;
        speech = model + speech;
      } else {
        model = self.current.model = "";
        cursor = -1;
      }

      var timeout = setTimeout(function nextChar() {
        var prevChar = model.charAt(cursor),
          newChar,
          newValue;

        if (mistaken) {
          invincible = self.getInvincibility();
          mistaken = false;
          newChar = null;
          newValue = model = model.substr(0, cursor);

          cursor--;
        } else {
          cursor++;

          newChar =
            --invincible < 0 && self.isMistaking()
              ? self.utils.randomChar()
              : speech.charAt(cursor);

          if (newChar !== speech.charAt(cursor)) mistaken = true;
          newValue = model += newChar;
        }

        self.set(newValue, [newValue, newChar, prevChar, speech]);

        if (mistaken || cursor < speech.length)
          timeout = setTimeout(nextChar, self.getSayingSpeed());
        else self.next();
      }, self.getSayingSpeed());

      return self;
    },

    erase: function (n) {
      var self = this,
        cursor =
          typeof self.current.model === "string"
            ? self.current.model.length
            : -1,
        min = typeof n === "number" && n < 0 ? cursor + 1 + n : 0;

      if (cursor < 0) return self.next();

      var timeout = setTimeout(function eraseChar() {
        var prevChar = self.current.model.charAt(cursor),
          newValue = self.current.model.substr(0, --cursor);

        self.set(newValue, [newValue, null, prevChar, newValue]);

        if (cursor >= min)
          setTimeout(eraseChar, self.getSayingSpeed(0.2, true));
        else self.next();
      }, self.getSayingSpeed(0.2, true));

      return self;
    },

    wait: function (delay) {
      var self = this;
      setTimeout(function () {
        self.next();
      }, delay);
      return self;
    },
  };

  w.TheaterJS = TheaterJS;
})(window, document);

const Typeit = (isAimee, emId) => {
  var theater = new TheaterJS();

  theater.describe("Luke", 0.9, emId);

  theater
    .on("*", function (eventName, originalEvent, sceneName, arg) {})
    .on("say:start, erase:start", function (eventName) {
      var self = this,
        current = self.current.voice;
      self.utils.addClass(current, "saying");
    })
    .on("say:end, erase:end", function (eventName) {
      var self = this,
        current = self.current.voice;
      self.utils.removeClass(current, "saying");
    });

  theater.write({ name: "call", args: [kill, true] }).write(function () {
    theater.play(true);
  });
  function kill() {
    return self;
  }
};

export { Typeit };
