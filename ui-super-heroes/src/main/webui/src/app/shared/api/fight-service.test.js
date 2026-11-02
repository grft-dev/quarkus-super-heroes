import {generateImage, getFights, getRandomFighters, getRandomLocation, narrateFight, startFight} from "./fight-service"
import axios from "axios"
import {getRandomHeroFromGraft} from "./heroes-graft-service"
import {getRandomVillainFromGraft} from "./villains-graft-service"
import {
  generateImageFromGraft,
  getFightsFromGraft,
  getRandomLocationFromGraft,
  narrateFightFromGraft,
  startFightFromGraft,
} from "./fights-graft-service"

jest.mock('axios')
jest.mock('./heroes-graft-service')
jest.mock('./villains-graft-service')
jest.mock('./fights-graft-service')

const fightersData = {
  hero: {
    name: 'Fallback hero',
    level: 1,
    picture: 'https://dummyimage.com/240x320/1e8fff/ffffff&text=Fallback+Hero',
    powers: 'Fallback hero powers'
  },
  villain: {
    name: 'Fallback villain',
    level: 42,
    picture: 'https://dummyimage.com/240x320/b22222/ffffff&text=Fallback+Villain',
    powers: 'Fallback villain powers'
  }
}

const fightersResponse = {
  data: fightersData
  ,
  status: 200,
  statusText: 'OK',
  headers: {
    'content-type': 'application/json;charset=UTF-8',
    'content-length': '314'
  },
  config: {
    transitional: {
      silentJSONParsing: true,
      forcedJSONParsing: true,
      clarifyTimeoutError: false
    },
    adapter: ['xhr', 'http'],

    headers: {
      Accept: 'application/json, text/plain, */*',
      'Content-Type': null
    },
    method: 'get',
    url: 'http://localhost:8082/api/fights/randomfighters',
    data: undefined
  },
}

const locationData = {
  name: "Mock location",
  picture: "https://dummyimage.com/240x320/1e8fff/ffffff&text=Mock",
  description: "Mock location description"
}

const locationResponse = {
  data: locationData,
  headers: {"content-length": "433", "content-type": "application/json;charset=UTF-8"}
}

const fightData = {
  fightDate: "2023-10-24T21:34:47.617598Z",
  id: 200,
  loserLevel: 1,
  loserName: "Mock hero",
  loserPicture: "https://dummyimage.com/240x320/1e8fff/ffffff&text=Mock+Hero",
  loserPowers: "Being fake",
  loserTeam: "heroes",
  winnerLevel: 42,
  winnerName: "Mock villain",
  winnerPicture: "https://dummyimage.com/240x320/b22222/ffffff&text=Mock+Villain",
  winnerPowers: "Dissimulation",
  winnerTeam: "villains",
  location: locationData
}

const fightResponse = {
  data: fightData,
  headers: {"content-length": "433", "content-type": "application/json;charset=UTF-8"}
}

const fightsList = {
  data: [fightData],
  headers: {"content-length": "433", "content-type": "application/json;charset=UTF-8"}
}

const narrationData = "It was a dark and stormy night"
const narrationResponse = {
  data: narrationData,
  headers: {"content-length": "433", "content-type": "text/plain"}
}

const narrationImageData = {
  imageUrl: "https://dummyimage.com/240x320/1e8fff/ffffff&text=Fallback+Image"
}

const narrationImageResponse = {
  data: narrationImageData,
  headers: {"content-length": "433", "content-type": "application/json;charset=UTF-8"}
}

describe("the fight service", () => {
  beforeEach(() => {
    jest.spyOn(console, 'error')
    console.error.mockImplementation(() => null)
  })

  afterEach(() => {
    console.error.mockRestore()
  })

  describe("getting random fighters", () => {

    beforeEach(() => {
      getRandomHeroFromGraft.mockResolvedValue(fightersData.hero)
      getRandomVillainFromGraft.mockResolvedValue(fightersData.villain)
    })

    afterEach(() => {
      jest.resetAllMocks()
    })

    it("loads fighters from graft", async () => {
      await getRandomFighters({})
      expect(getRandomHeroFromGraft).toHaveBeenCalled()
      expect(getRandomVillainFromGraft).toHaveBeenCalled()
    })

    it("returns fighters", async () => {
      const answer = await getRandomFighters({})
      expect(answer).toStrictEqual(fightersData)
    })

    describe("when graft clients fail", () => {
      beforeEach(() => {
        getRandomHeroFromGraft.mockRejectedValue(new Error('Deliberate error'))
        getRandomVillainFromGraft.mockRejectedValue(new Error('Deliberate error'))
      })

      afterEach(() => {
        jest.resetAllMocks()
      })

      it("returns undefined", async () => {
        const answer = await getRandomFighters({})
        expect(answer).toBeUndefined()
      })
    })
  })

  describe("getting random location", () => {

    beforeEach(() => {
      getRandomLocationFromGraft.mockResolvedValue(locationData)
    })

    afterEach(() => {
      jest.resetAllMocks()
    })

    it("uses fights graft", async () => {
      await getRandomLocation({})
      expect(getRandomLocationFromGraft).toHaveBeenCalled()
    })

    it("returns location", async () => {
      const answer = await getRandomLocation({})
      expect(answer).toStrictEqual(locationData)
    })

    describe("when graft and REST fail", () => {
      beforeEach(() => {
        getRandomLocationFromGraft.mockRejectedValue(new Error('Deliberate error'))
        axios.get.mockRejectedValue(new Error('Deliberate error'))
      })

      afterEach(() => {
        jest.resetAllMocks()
      })

      it("gets new location", async () => {
        const answer = await getRandomLocation({})
        expect(answer).toBeUndefined()
      })
    })
  })

  describe("triggering a fight", () => {
    beforeEach(() => {
      startFightFromGraft.mockResolvedValue(fightData)
    })

    afterEach(() => {
      jest.resetAllMocks()
    })

    it("uses fights graft", async () => {
      const response = await startFight(fightersData)
      expect(startFightFromGraft).toHaveBeenCalled()
      expect(response).toStrictEqual(fightData)
    })
  })

  describe("narrating a fight", () => {

    beforeEach(() => {
      narrateFightFromGraft.mockResolvedValue(narrationData)
    })

    afterEach(() => {
      jest.resetAllMocks()
    })

    it("uses fights graft", async () => {
      await narrateFight(fightData)
      expect(narrateFightFromGraft).toHaveBeenCalled()
    })

    it("narrates the fight", async () => {
      const answer = await narrateFight(fightData)
      expect(answer).toStrictEqual(narrationData)
    })
  })

  describe("generating an image from a fight narration", () => {

    beforeEach(() => {
      generateImageFromGraft.mockResolvedValue(narrationImageData)
    })

    afterEach(() => {
      jest.resetAllMocks()
    })

    it("uses fights graft", async () => {
      await generateImage(narrationData, "https://winner.png", "https://loser.png")
      expect(generateImageFromGraft).toHaveBeenCalled()
    })

    it("generates an image", async () => {
      const answer = await generateImage(narrationData, null, null)
      expect(answer).toStrictEqual(narrationImageData)
    })
  })

  describe("listing fights", () => {
    beforeEach(() => {
      getFightsFromGraft.mockResolvedValue([fightData])
    })

    afterEach(() => {
      jest.resetAllMocks()
    })

    it("uses fights graft by default", async () => {
      await getFights()
      expect(getFightsFromGraft).toHaveBeenCalled()
    })

    it("returns the data", async () => {
      const answer = await getFights()
      expect(answer).toStrictEqual([fightData])
    })

    it("passes page and size to graft", async () => {
      await getFights({page: 10, size: 100})
      expect(getFightsFromGraft).toHaveBeenCalledWith({page: 10, size: 100})
    })

    it("returns an empty list when graft has no rows", async () => {
      getFightsFromGraft.mockResolvedValue([])
      const answer = await getFights({page: 10, size: 100})
      expect(answer).toStrictEqual([])
    })
  })
})
