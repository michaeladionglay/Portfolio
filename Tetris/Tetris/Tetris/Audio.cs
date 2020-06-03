using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tetris
{
    class Music
    {
        IrrKlang.ISoundEngine AudioEngine = new IrrKlang.ISoundEngine();

        IrrKlang.ISound music = null; //controls volume
        IrrKlang.ISound soundEffect = null;
        public float vol_delta = 0.005f;

        IrrKlang.ISoundSource srcShoot = null;
        IrrKlang.ISoundSource srcLine = null;
        IrrKlang.ISoundSource srcSelection = null;
        IrrKlang.ISoundSource srcClear = null;
        IrrKlang.ISoundSource srcFall = null;

        IrrKlang.ISound sndShoot = null;

        
        public IrrKlang.ISoundEngine getAE()
        {
            return AudioEngine;
        }

        public void init()
        {
            srcShoot = AudioEngine.AddSoundSourceFromFile("shoot.wav");
            srcLine = AudioEngine.AddSoundSourceFromFile("line.wav");
            srcSelection = AudioEngine.AddSoundSourceFromFile("selection.wav");
            srcClear = AudioEngine.AddSoundSourceFromFile("clear.wav");
            srcFall = AudioEngine.AddSoundSourceFromFile("fall.wav");
        }

        public void themeSong()
        {
            music = AudioEngine.Play2D("theme.wav", true);
            music.Volume = 0.2f;
        }

        public IrrKlang.ISound rotateSound()
        {
            soundEffect = AudioEngine.Play2D("line.wav");
            sndShoot = AudioEngine.Play2D(srcLine, false, false, false);
            return sndShoot;
        }

        public void normalPlace()
        {
            soundEffect = AudioEngine.Play2D("selection.wav");
            sndShoot = AudioEngine.Play2D(srcSelection, false, false, false);
        }

        public void fastPlace()
        {
            soundEffect = AudioEngine.Play2D("shoot.wav");
            sndShoot = AudioEngine.Play2D(srcShoot, false, false, false);
        }

        public void completeLine()
        {
            soundEffect = AudioEngine.Play2D("clear.wav");
            sndShoot = AudioEngine.Play2D(srcClear, false, false, false);
        }

        public void gameOver()
        {
            soundEffect = AudioEngine.Play2D("fall.wav");
            sndShoot = AudioEngine.Play2D(srcFall, false, false, false);
        }
    }
}
