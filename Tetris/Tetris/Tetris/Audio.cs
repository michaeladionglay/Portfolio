using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tetris
{
    class Music
    {
        public IrrKlang.ISoundEngine AudioEngine = new IrrKlang.ISoundEngine();

        public IrrKlang.ISound music = null; //controls volume
        public IrrKlang.ISound soundEffect = null;
        public float vol_delta = 0.005f;

        public IrrKlang.ISoundSource srcShoot = null;
        public IrrKlang.ISoundSource srcLine = null;
        public IrrKlang.ISoundSource srcSelection = null;
        public IrrKlang.ISoundSource srcClear = null;
        public IrrKlang.ISoundSource srcFall = null;

        public IrrKlang.ISound sndRotate = null;
        public IrrKlang.ISound sndShoot = null;

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

        public float VolSoundEffect()
        {
            return sndShoot.Volume;
        }

        public void themeSong()
        {
            music = AudioEngine.Play2D("theme.wav", true);
            music.Volume = 0.2f;
        }

        public void rotateSound()
        {
            sndRotate = AudioEngine.Play2D("line.wav");
            sndShoot = AudioEngine.Play2D(srcLine, false, false, false);
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